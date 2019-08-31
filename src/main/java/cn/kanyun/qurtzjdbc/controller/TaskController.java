package cn.kanyun.qurtzjdbc.controller;

import cn.kanyun.qurtzjdbc.entity.JobEntity;
import cn.kanyun.qurtzjdbc.entity.Result;
import cn.kanyun.qurtzjdbc.quartz.QuartzService;
import cn.kanyun.qurtzjdbc.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @author Kanyun
 * @date 2019/6/21
 */
@Slf4j
@Controller
@RequestMapping("/task")
public class TaskController {

    @Resource
    private TaskService taskService;
    @Resource
    private QuartzService quartzService;

    /**
     * 任务列表
     *
     * @return
     */
    @GetMapping(value = {"/", "list", ""})
    public String list() {
        log.debug("[{}]进入定时任务列表页", LocalDateTime.now());
        return "task";
    }

    /**
     * 任务添加
     *
     * @return
     */
    @PostMapping("/add")
    public Result addTask(JobEntity entity) {
        Result result = new Result();
        return result;
    }
}
