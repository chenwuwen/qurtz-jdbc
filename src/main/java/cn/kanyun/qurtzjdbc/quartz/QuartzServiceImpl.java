package cn.kanyun.qurtzjdbc.quartz;

import cn.kanyun.qurtzjdbc.entity.JobEntity;

import cn.kanyun.qurtzjdbc.entity.JobStatus;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.simpl.SimpleThreadPool;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author Kanyun
 * @date 2019/6/17
 */
@Service
@Slf4j
public class QuartzServiceImpl implements QuartzService {

    /**
     * Scheduler作为Quartz的核心调度器，有将近50多个API接口，包括任务的添加，暂停，恢复，删除等一系列的API
     */
    @Resource
    @Qualifier("scheduler")
    private Scheduler scheduler;

    @Override
    public List<JobEntity> loadJobs() {
        List<JobEntity> jobs = new ArrayList<>();
        try {
            List<String> triggerGroups = scheduler.getTriggerGroupNames();
            for (String triggerGroupName : triggerGroups) {
                Set<TriggerKey> triggerKeySet = scheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals(triggerGroupName));
                for (TriggerKey triggerKey : triggerKeySet) {
                    Trigger t = scheduler.getTrigger(triggerKey);
                    if (t instanceof CronTrigger) {
                        CronTrigger trigger = (CronTrigger) t;
                        JobKey jobKey = trigger.getJobKey();
                        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                        JobEntity entity = new JobEntity();
                        entity.setJobName(jobKey.getName());
                        entity.setJobGroupName(jobKey.getGroup());
                        entity.setTriggerName(triggerKey.getName());
                        entity.setTriggerGroupName(triggerKey.getGroup());
                        entity.setCronExpression(trigger.getCronExpression());
                        entity.setNextFireTime(trigger.getNextFireTime());
                        entity.setPreviousFireTime(trigger.getPreviousFireTime());
                        entity.setJobClassName(jobDetail.getJobClass().getCanonicalName());
//                        任务状态(一共6种状态,保存在枚举中,我自己也定义了一个枚举,一一对应)
                        Trigger.TriggerState state = scheduler.getTriggerState(trigger.getKey());
//                        因为得到的状态是qurtz中的枚举(他的枚举中没有文字说明),因此这里将qurtz中枚举转换到自己定义的枚举中
                        cn.kanyun.qurtzjdbc.entity.JobStatus status = cn.kanyun.qurtzjdbc.entity.JobStatus.valueOf(state.name());
                        entity.setJobStatus(status.toString());
//                        这个JobDataMap肯定不为null,但是size可能是0
                        JobDataMap map = scheduler.getJobDetail(jobKey).getJobDataMap();
                        if (null != map.get("count")) {
//                            这个map应该是需要自行设置的,在创建任务时
                        }
                        jobs.add(entity);
                    }
                }
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return jobs;
    }

    @Override
    public void addJob(JobEntity entity) throws SchedulerException, ClassNotFoundException, IllegalAccessException, InstantiationException {

//        创建jobDetail实例，绑定Job实现类,指明job的名称，所在组的名称，以及绑定job类
        Class<? extends Job> jobClass = (Class<? extends Job>) (Class.forName(entity.getJobClassName()).newInstance()
                .getClass());
//        任务名称和组构成任务key
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(entity.getJobName(), entity.getJobGroupName())
                .build();
//             定义调度触发规则
//             使用cornTrigger规则
//             触发器key
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(entity.getJobName(), entity.getJobGroupName())
                .startAt(DateBuilder.futureDate(1, DateBuilder.IntervalUnit.SECOND))
                .withSchedule(CronScheduleBuilder.cronSchedule(entity.getCronExpression())).startNow().build();
//             把作业和触发器注册到任务调度中
        scheduler.scheduleJob(jobDetail, trigger);
//             启动
        if (!scheduler.isShutdown()) {
            scheduler.start();
        }
    }


    @Override
    public boolean updateJobTime(JobEntity oldJob, String newCron) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(oldJob.getJobName(), oldJob.getJobGroupName());
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(newCron);
        trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
        Date date = scheduler.rescheduleJob(triggerKey, trigger);
        return date != null;
    }

    @Override
    public void pauseJob(JobEntity entity) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(entity.getJobName(), entity.getJobGroupName());
        scheduler.pauseJob(jobKey);
    }

    @Override
    public void resumeJob(JobEntity entity) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(entity.getJobName(), entity.getJobGroupName());
        scheduler.resumeJob(jobKey);
    }

    @Override
    public void removeJob(JobEntity entity) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(entity.getJobName(), entity.getJobGroupName());
        scheduler.deleteJob(jobKey);
    }

    @Override
    public void runJobNow(JobEntity entity) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(entity.getJobName(), entity.getJobGroupName());
        scheduler.triggerJob(jobKey);
    }

    @Override
    public JobStatus getJobStatus(JobEntity entity) {
        TriggerKey triggerKey = TriggerKey.triggerKey(entity.getJobName(), entity.getJobGroupName());
        try {
            Trigger.TriggerState triggerState = scheduler.getTriggerState(triggerKey);
            return JobStatus.valueOf(triggerState.name());
        } catch (SchedulerException e) {
            e.printStackTrace();
            return JobStatus.ERROR;
        }

    }
}
