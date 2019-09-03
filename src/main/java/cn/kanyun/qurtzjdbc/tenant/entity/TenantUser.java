package cn.kanyun.qurtzjdbc.tenant.entity;

import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author Kanyun
 */
@Data
public class TenantUser implements Serializable {

    private Long id;
    private String userName;
    private String password;
}
