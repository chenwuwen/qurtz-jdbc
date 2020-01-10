package cn.kanyun.qurtzjdbc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Kanyun
 * @date 2019/6/21
 */
@Data
@AllArgsConstructor
public class Result implements Serializable {
    private String msg;
    private Integer code;
    private Object data;


    public static Result successHandler() {
        return new Result("处理成功", CodeEnum.HANDLER_SUCCESS.getCode(), null);
    }

    public static Result successHandler(Object o) {
        return new Result("处理成功", CodeEnum.HANDLER_SUCCESS.getCode(), o);
    }


    public static Result errorHandler(String msg) {
        return new Result(msg, CodeEnum.HANDLER_ERROR.getCode(), null);
    }
}
