package cn.kanyun.qurtzjdbc.controller;

import cn.kanyun.qurtzjdbc.entity.JobEntity;
import cn.kanyun.qurtzjdbc.entity.JobStatus;
import cn.kanyun.qurtzjdbc.entity.Result;
import cn.kanyun.qurtzjdbc.quartz.QuartzService;
import cn.kanyun.qurtzjdbc.service.TaskService;
import cn.kanyun.qurtzjdbc.util.BeetlTemplateUtil;
import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

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
     * 任务页
     * 需要注意的是,使用beetl的ajax时,如果返回的 页面 不包括 #
     * 那么beetl就不会理会#ajax name : {}这个标记，而直接渲染整个页面，#ajax name : {}大括号里面的东西同样会被渲染
     *
     * @return
     */
    @GetMapping(value = {"/", ""})
    public String page(Model model, HttpServletRequest request) {
        log.debug("[{}]进入定时任务列表页", LocalDateTime.now());
        return "task";
    }

    /**
     * 任务列表
     * 使用Beetl的Ajax请求
     * https://blog.csdn.net/hiredme/article/details/44219581
     *
     * @return
     */
    @GetMapping(value = {"/list"})
    public String list(Model model, HttpServletRequest request) {
        log.debug("[{}]Ajax任务列表获取", LocalDateTime.now());
        List<JobEntity> jobEntities = quartzService.loadJobs();
//        model.addAttribute("jobEntities", jobEntities);
//        如果jobEntities为null,使用 request.setAttribute() beetl模板会提示找不到jobEntities变量,使用model.addAttribute()则不会出现该问题,因此使用model更加可靠一些
        request.setAttribute("jobEntities", jobEntities);
        return "task#list";
    }

    /**
     * 返回添加任务的表单,用作Iframe
     *
     * @return
     */
    @GetMapping(value = {"/iframe"})
    public String iframe() {
        return "element/addTask";
    }


    /**
     * 下载任务模板
     * https://blog.csdn.net/xiongyouqiang/article/details/80488202
     */
    @GetMapping(value = "/downloadTaskTemplate")
    public ResponseEntity<byte[]> downloadTaskTemplate(HttpServletResponse response) throws IOException {
        File file = BeetlTemplateUtil.generateJobTemplateFile(8);
        byte[] body = Files.toByteArray(file);
//        将文件压缩
//        byte[] body = ZipUtil.packEntry(file);
        HttpHeaders headers = new HttpHeaders();
//        设置MIME类型 内容是字节流
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//        设置下载的附件
//        ContentDisposition contentDisposition =
//                ContentDisposition.builder("form-data")
//                        .filename(file.getName(), Charset.defaultCharset())
//                        .build();
//        headers.setContentDisposition(contentDisposition);
        headers.add("Content-Disposition", "attachment;fileName=" + file.getName());
        HttpStatus statusCode = HttpStatus.OK;
        ResponseEntity<byte[]> entity = new ResponseEntity<>(body, headers, statusCode);
        return entity;
    }

    /**
     * 任务添加
     *
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public Result addTask(HttpServletRequest request, JobEntity entity, @RequestParam(value = "jobFile", required = true) MultipartFile jobFile) {
        File tempDir = Files.createTempDir();
        try {
            String fileName = jobFile.getOriginalFilename();
            if (fileName.endsWith(".java")) {
                String filePath = tempDir.getAbsolutePath() + File.separator + fileName;
                jobFile.transferTo(new File(filePath));
//                判断是否是java文件的同时,如果是java文件的同时也将该文件进行了编译,并且保存了.class文件到项目的classpath目录下
                if (taskService.isJavaFile(filePath)) {
//                    如果是java文件,此时就可以直接反射获取对象了
                    Class clazz = Class.forName(entity.getJobClassName());
//                    这一步很重要,将Class对象转换为Object,需要这个类有空参的构造方法
                    Object o = clazz.newInstance();
//                    再判断这个反射后的对象是不是org.quartz.Job的子类
                    if (taskService.isStander(o)) {
                        taskService.insertJob(entity);
                        return Result.successHandler();
                    }
                    return Result.errorHandler("提交的java文件不是标准的Qurtz Job");
                }
                return Result.errorHandler("提交的文件不是Java文件");
            }
            return Result.errorHandler("提交的文件不是Java文件");
        } catch (IOException | SchedulerException | IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            e.printStackTrace();
            return Result.errorHandler(e.getMessage());
        }
    }

    /**
     * 暂停任务
     *
     * @param jobEntity 至少包含jobName、jobGroupName
     */
    @PostMapping("/pauseTask")
    @ResponseBody
    public Result pauseTask(JobEntity jobEntity) {
        try {
            quartzService.pauseJob(jobEntity);
            JobStatus status = quartzService.getJobStatus(jobEntity);
            return Result.successHandler(status.toString());
        } catch (SchedulerException e) {
            e.printStackTrace();
            return Result.errorHandler(e.getMessage());
        }
    }

    /**
     * 恢复任务
     * 前提是任务时暂停状态
     *
     * @param jobEntity 至少包含jobName、jobGroupName
     */
    @PostMapping("/resumeTask")
    @ResponseBody
    public Result resumeTask(JobEntity jobEntity) {
        try {
            quartzService.resumeJob(jobEntity);
            JobStatus status = quartzService.getJobStatus(jobEntity);
            return Result.successHandler(status.toString());
        } catch (SchedulerException e) {
            e.printStackTrace();
            return Result.errorHandler(e.getMessage());
        }
    }

    /**
     * 开始任务
     *
     * @param jobEntity 至少包含jobName、jobGroupName
     */
    @PostMapping("/runTask")
    @ResponseBody
    public Result runTask(JobEntity jobEntity) {
        try {
            quartzService.runJobNow(jobEntity);
            JobStatus status = quartzService.getJobStatus(jobEntity);
            return Result.successHandler(status.toString());
        } catch (SchedulerException e) {
            e.printStackTrace();
            return Result.errorHandler(e.getMessage());
        }
    }


}
