package cn.kanyun.qurtzjdbc.entity;

import lombok.Data;

import java.util.Map;

/**
 * @author Kanyun
 * @date 2019/6/21
 */
@Data
public class Result {
    private String msg;
    private Integer code;
    private Map data;
}
