package cn.kanyun.qurtzjdbc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * 使用MybatisPlus
 * 实体类中添加@TableName、@TableId
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
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField
    private String userName;
    @TableField
    private String password;
    @TableField
    private Integer age;
    @TableField
    private String area;
    @TableField
    private Long phone;
    @TableField
    private Long registerDate;
    @TableField
    private Long registerIp;

}