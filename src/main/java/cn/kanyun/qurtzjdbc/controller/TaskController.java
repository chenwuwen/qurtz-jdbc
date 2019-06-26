package cn.kanyun.qurtzjdbc.controller;

import cn.kanyun.qurtzjdbc.entity.JobEntity;
import cn.kanyun.qurtzjdbc.entity.Result;
import cn.kanyun.qurtzjdbc.quartz.QuartzService;
import cn.kanyun.qurtzjdbc.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 *
 * @author Kanyun
 * @date 2019/6/21
 */
@RestController("task")
public class TaskController {

    @Resource
    private TaskService taskService;
    @Resource
    private QuartzService quartzService;
    /**
     * 任务添加
     * @return
     */
    @PostMapping("/add")
    public Result addTask(JobEntity entity) {
        Result result = new Result();
        return result;
    }
}
