package cn.kanyun.qurtzjdbc.listener;

import cn.kanyun.qurtzjdbc.entity.JobEntity;
import cn.kanyun.qurtzjdbc.jobs.JobDemo;
import cn.kanyun.qurtzjdbc.quartz.QuartzService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * SpringBoot中CommandLineRunner的作用
 * 平常开发中有可能需要实现在项目启动后执行的功能，SpringBoot提供的一种简单的实现方案就是
 * 添加一个model并实现CommandLineRunner接口，实现功能的代码放在实现的run方法中(在spring中可以通过实现InitializingBean接口,实现afterPropertiesSet()方法,来实现类似效果,或者实现ApplicationListener)
 * 如果需要按照一定的顺序去执行，那么就需要在实体类上使用一个@Order注解（或者实现Order接口）来表明顺序
 * 在整个应用生命周期内只会执行一次。
 * https://www.cnblogs.com/chenpi/p/9696310.html
 *
 * @author Kanyun
 * @date 2019/6/17
 */
@Component
@Order(value = 10)
@Slf4j
public class ScheduleJobInitListener implements CommandLineRunner {

    /**
     * 注入Quartz Service
     */
    @Resource
    private QuartzService quartzService;

    @Override
    public void run(String... strings) throws Exception {
        log.info("======= 系统启动时,插入一条Job Demo ========");
        JobEntity jobEntity = new JobEntity();
        jobEntity.setJobName("JobDemo 测试");
        jobEntity.setCronExpression("0/10 * * * * ?");
        jobEntity.setJobClassName("cn.kanyun.qurtzjdbc.jobs.JobDemo");
        jobEntity.setJobGroupName("Demo");
//        插入新任务
        quartzService.addJob(jobEntity);
//        立即运行任务
        quartzService.runJobNow(jobEntity);
    }
}
