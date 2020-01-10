package cn.kanyun.qurtzjdbc.entity;


/**
 * 状态码枚举
 */
public enum CodeEnum {

    /**
     * 请求处理成功
     */
    HANDLER_SUCCESS(1001),

    /**
     * 请求处理失败
     */
    HANDLER_ERROR(1002);

    /**
     * 状态值
     */
    private int code;

    CodeEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
