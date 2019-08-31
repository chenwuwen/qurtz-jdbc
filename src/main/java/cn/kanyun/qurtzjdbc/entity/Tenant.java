package cn.kanyun.qurtzjdbc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "TENANT")
public class Tenant {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField
    private String tenantName;

    @TableField
    private String domain;

    @TableField
    private String dbUrl;

    @TableField
    private String dbUser;

    @TableField
    private String dbPass;

    @TableField
    private Long registerDate;
}