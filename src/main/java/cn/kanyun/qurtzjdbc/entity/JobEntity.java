package cn.kanyun.qurtzjdbc.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @author KANYUN
 */
@Data
public class JobEntity {

    private Integer id;
    /**
     * 任务名
     */
    @NotBlank
    private String jobName;
    /**
     * 任务组名
     */
    @NotBlank
    private String jobGroupName;
    /**
     * 触发器名
     */
    @NotBlank
    private String triggerName;
    /**
     * 触发器组名
     */
    @NotBlank
    private String triggerGroupName;
    /**
     * Cron表达式
     */
    @NotBlank
    private String cronExpression;
    /**
     * Job类名
     */
    @NotBlank
    private String jobClassName;
    /**
     * 运行次数
     */
    private Long count;
    /**
     * Job状态
     */
    private String jobStatus;
    /**
     * 上次运行时间
     */
    private Date previousFireTime;
    /**
     * 下次运行时间
     */
    private Date nextFireTime;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;

}