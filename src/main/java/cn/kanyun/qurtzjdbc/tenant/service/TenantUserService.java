package cn.kanyun.qurtzjdbc.tenant.service;

import cn.kanyun.qurtzjdbc.entity.Tenant;
import cn.kanyun.qurtzjdbc.service.BaseService;
import cn.kanyun.qurtzjdbc.tenant.DynamicDataSource;
import cn.kanyun.qurtzjdbc.tenant.entity.TenantUser;

import java.util.Optional;

/**
 * @author Kanyun
 */
public interface TenantUserService extends BaseService<Long, TenantUser> {
    /**
     * 租户登录
     * @param tenantUser
     */
    Optional<TenantUser> login(TenantUser tenantUser);
}
