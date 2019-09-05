package cn.kanyun.qurtzjdbc.tenant.service.impl;

import cn.kanyun.qurtzjdbc.service.impl.BaseServiceImpl;
import cn.kanyun.qurtzjdbc.tenant.DynamicDataSource;
import cn.kanyun.qurtzjdbc.tenant.entity.TenantUser;
import cn.kanyun.qurtzjdbc.tenant.mapper.TenantUserMapper;
import cn.kanyun.qurtzjdbc.tenant.service.TenantUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * 需要注意的是@DynamicDataSource自定义的注解设置在接口上不生效的,
 * 即使是在注解定义中添加了@Inherited元注解
 * https://www.jianshu.com/p/7f54e7250be3
 * @author Kanyun
 */
@Service
@DynamicDataSource
public class TenantUserServiceImpl extends BaseServiceImpl<Long, TenantUser> implements TenantUserService {
    @Resource
    private TenantUserMapper tenantUserMapper;


    @Override
    public Optional<TenantUser> login(TenantUser tenantUser) {
        QueryWrapper<TenantUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("USER_NAME", tenantUser.getUserName()).eq("PASSWORD", tenantUser.getPassword());
        TenantUser user = tenantUserMapper.selectOne(queryWrapper);
        return Optional.ofNullable(user);
    }


}
