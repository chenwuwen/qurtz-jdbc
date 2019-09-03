package cn.kanyun.qurtzjdbc.tenant.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Kanyun
 */
@Data
public class TenantBiz implements Serializable {
    private Long id;
    private String bizName;
    private String bizComment;
}
