package cn.kanyun.qurtzjdbc.jobs;

import org.quartz.*;
import org.springframework.stereotype.Component;

/**
 * @author kanyun
 * 测试添加的Job文件,在项目启动后,新增定时任务,上传该文件,将自动编译,并添加到定时任务
 * 需要注意的是,因为上传的是java文件,所以进行编译的时候会查找相应的依赖,也就是本类中import的依赖
 * 所以要确保上传的文件中的依赖,在项目中存在,否则编译不会通过
 * @date 2019/6/17
 */
@DisallowConcurrentExecution
@Component
public class TestAddJob implements Job {

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
        System.out.println("====TestAddJob执行任务===");
    }


}
