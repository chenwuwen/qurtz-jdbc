package cn.kanyun.qurtzjdbc.service.impl;

import cn.kanyun.qurtzjdbc.dao.TenantMapper;
import cn.kanyun.qurtzjdbc.entity.Tenant;
import cn.kanyun.qurtzjdbc.service.TenantService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author KANYUN
 */
@Service
public class TenantServiceImpl extends BaseServiceImpl<Long, Tenant> implements TenantService {
    @Resource
    private TenantMapper tenantMapper;

}