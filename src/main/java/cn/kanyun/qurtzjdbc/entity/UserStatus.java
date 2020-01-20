package cn.kanyun.qurtzjdbc.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 用户状态枚举
 */
public enum UserStatus {
    NORMAL(1, "正常"), LOCK(2, "锁定"), DELETE(3, "删除");

    @EnumValue //标记数据库存的值是status
    @JsonValue //标记响应json值
    private int status;
    private String description;

    UserStatus(int status, String description) {
        this.status = status;
        this.description = description;
    }


    public static void main(String[] args) {
        System.out.println(UserStatus.values());
    }
}
