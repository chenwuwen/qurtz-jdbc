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
public class Result<T> implements Serializable {
    private String msg;
    private Integer code;
    private Object data;
    private Integer count;


    public static Result successHandler() {
        return new Result("处理成功", CodeEnum.HANDLER_SUCCESS.getCode(), null, 0);
    }

    public static Result successHandler(Object o) {
        return new Result("处理成功", CodeEnum.HANDLER_SUCCESS.getCode(), o, 0);
    }

    public static Result successHandler(Object o, Integer count) {
        return new Result("处理成功", CodeEnum.HANDLER_SUCCESS.getCode(), o, count);
    }


    public static Result errorHandler(String msg) {
        return new Result(msg, CodeEnum.HANDLER_ERROR.getCode(), null, 0);
    }
}
