package cn.kanyun.qurtzjdbc.tenant.service;

import cn.kanyun.qurtzjdbc.service.BaseService;
import cn.kanyun.qurtzjdbc.tenant.DynamicDataSource;
import cn.kanyun.qurtzjdbc.tenant.entity.TenantBiz;

import java.util.List;

/**
 * @author Kanyun
 */
public interface TenantBizService extends BaseService<Long, TenantBiz> {

    /**
     * 重写父类方法,主要是因为AOP无法拦截父类方法
     * 但是子类可以重写父类方法,然后AOP可以拦截到重写行为
     * 并且进行一定处理，此时子类再直接调用父类方法就可以了
     * @return
     */
    @Override
    List<TenantBiz> queryAll();
}
