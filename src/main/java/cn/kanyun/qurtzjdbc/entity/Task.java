package cn.kanyun.qurtzjdbc.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author KANYUN
 */
@Data
@Builder
public class Task {
    private Integer id;
    private String taskName;
    private String content;
    private LocalDateTime date;

}