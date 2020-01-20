package cn.kanyun.qurtzjdbc.entity;

import lombok.Data;

/**
 * 日志消息实体
 */
@Data
public class LoggerMessage {
    private String body;
    private String timestamp;
    private String threadName;
    private String className;
    private String level;
}
