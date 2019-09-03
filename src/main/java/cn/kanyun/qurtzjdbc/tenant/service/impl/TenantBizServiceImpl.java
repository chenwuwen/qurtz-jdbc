package cn.kanyun.qurtzjdbc.tenant.service.impl;

import cn.kanyun.qurtzjdbc.service.impl.BaseServiceImpl;
import cn.kanyun.qurtzjdbc.tenant.DynamicDataSource;
import cn.kanyun.qurtzjdbc.tenant.entity.TenantBiz;
import cn.kanyun.qurtzjdbc.tenant.mapper.TenantBizMapper;
import cn.kanyun.qurtzjdbc.tenant.service.TenantBizService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Kanyun
 */
@Service
@DynamicDataSource
public class TenantBizServiceImpl extends BaseServiceImpl<Long, TenantBiz> implements TenantBizService {
    @Resource
    private TenantBizMapper tenantBizMapper;


    @Override
    public List<TenantBiz> queryAll() {
//        由于重写了父类的方法,所以可以被AOP拦截到,又因为重写的实现只是调用了父类的方法,所以实际上只是代码冗余了
//        但相比直接拦截父类,性能上要好很多,因为有些请求并非都需要(经过AOP)切换数据源
        return super.queryAll();
    }
}
