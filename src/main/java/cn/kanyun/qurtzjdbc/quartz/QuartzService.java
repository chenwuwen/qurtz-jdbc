package cn.kanyun.qurtzjdbc.quartz;

import cn.kanyun.qurtzjdbc.entity.JobEntity;
import cn.kanyun.qurtzjdbc.entity.JobStatus;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * @author Kanyun
 * @date 2019/6/17
 */
public interface QuartzService {

    /**
     * 从数据库中加载获取到所有Job
     *
     * @return
     */
    List<JobEntity> loadJobs();

    /**
     * 添加一个任务
     */
    void addJob(JobEntity entity) throws SchedulerException, ClassNotFoundException, IllegalAccessException, InstantiationException;

    /**
     * 修改任务执行时间
     *
     * @param oldJob  旧任务
     * @param newCron 新的cron表达式
     * @return
     */
    boolean updateJobTime(JobEntity oldJob, String newCron) throws SchedulerException;

    /**
     * 暂停一个任务
     *
     * @param entity
     */
    void pauseJob(JobEntity entity) throws SchedulerException;

    /**
     * 恢复指定的任务
     *
     * @param entity
     */
    void resumeJob(JobEntity entity) throws SchedulerException;

    /**
     * 删除任务
     *
     * @param entity
     */
    void removeJob(JobEntity entity) throws SchedulerException;

    /**
     * 立即触发job
     *
     * @param entity
     */
    void runJobNow(JobEntity entity) throws SchedulerException;

    /**
     * 得到任务的运行状态
     *
     * @param entity
     * @return
     */
    JobStatus getJobStatus(JobEntity entity);
}
