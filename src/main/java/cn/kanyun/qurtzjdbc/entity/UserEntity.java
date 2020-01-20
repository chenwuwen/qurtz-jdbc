package cn.kanyun.qurtzjdbc.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 使用MybatisPlus
 * 实体类中添加@TableName、@TableId
 *
 * @author KANYUN
 */
@Data
@TableName(value = "USER")
public class UserEntity implements Serializable {

    /**
     * 指定自增策略
     * value与数据库主键列名一致，若实体类属性名与表主键列名一致可省略value
     * 若没有开启驼峰命名，或者表中列名不符合驼峰规则，可通过该注解指定数据库表中的列名，exist标明数据表中有没有对应列
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    @TableField(value = "USER_NAME")
    private String userName;

    /**
     * 密码
     */
    @TableField(value = "PASSWORD")
    private String password;

    /**
     * 年龄
     */
    @TableField(value = "AGE")
    private Integer age;

    /**
     * 区域
     */
    @TableField(value = "AREA")
    private String area;

    /**
     * 电话
     */
    @TableField(value = "PHONE")
    private Long phone;

    /**
     * 状态 枚举类型
     * {@link cn.kanyun.qurtzjdbc.entity.UserStatus}
     */
    @JSONField(serialzeFeatures = SerializerFeature.WriteEnumUsingToString)
    @TableField(value = "STATUS")
    private UserStatus status;

    /**
     * 注册日期 时间戳
     */
    @TableField(value = "REGISTER_DATE", fill = FieldFill.INSERT_UPDATE)
    private Long registerDate;

    /**
     * 注册IP long类型
     */
    @TableField(value = "REGISTER_IP")
    private Long registerIp;

    /**
     * 验证码,非数据库字段
     */
    @TableField(exist = false)
    private String captcha;

}