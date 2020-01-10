package cn.kanyun.qurtzjdbc.jobs;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Component;

/**
 * @author kanyun
 * @DisallowConcurrentExecution : 此标记用在实现Job的类上面,意思是不允许并发执行.
 * 注意org.quartz.threadPool.threadCount线程池中线程的数量至少要多个,否则@DisallowConcurrentExecution不生效
 * 假如Job的设置时间间隔为3秒,但Job执行时间是5秒,
 * 设置@DisallowConcurrentExecution以后程序会等任务执行完毕以后再去执行,否则会在3秒时再启用新的线程执行
 * @date 2019/6/17
 */
@DisallowConcurrentExecution
@Component
@Slf4j
public class JobDemo implements Job {

    /**
     * 核心方法,Quartz Job真正的执行逻辑
     *
     * @param jobExecutionContext JobExecutionContext中封装有Quartz运行所需要的所有信息
     * @throws JobExecutionException 只允许抛出JobExecutionException异常
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //JobDetail中的JobDataMap是共用的,从getMergedJobDataMap获取的JobDataMap是全新的对象
        JobDataMap map = jobExecutionContext.getMergedJobDataMap();
        log.info("====JobDemo执行任务===");
    }


}
