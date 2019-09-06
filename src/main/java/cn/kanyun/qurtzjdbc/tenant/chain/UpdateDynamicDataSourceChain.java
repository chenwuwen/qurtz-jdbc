package cn.kanyun.qurtzjdbc.tenant.chain;

import cn.kanyun.qurtzjdbc.entity.Tenant;
import cn.kanyun.qurtzjdbc.tenant.datasource.TenantDataSourceCache;
import cn.kanyun.qurtzjdbc.tenant.datasource.TenantDynamicRoutingDataSource;
import cn.kanyun.qurtzjdbc.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentMap;

/**
 * 该类是责任链的一环,当添加@Component()注解后，会报循环依赖错误
 * 加入@Lazy()注解可以解决，但是使用@Resource注入的对象却是Null
 * 更新动态数据库连接池
 *
 * @author Kanyun
 */
@Component()
@Lazy()
@Slf4j
public class UpdateDynamicDataSourceChain extends TenantInitChain {

    @Resource
    private TenantDynamicRoutingDataSource tenantDynamicRoutingDataSource;
    @Resource
    private TenantDataSourceCache tenantDataSourceCache;


    public UpdateDynamicDataSourceChain(TenantInitChain nextTenantInitChain) {
        super(nextTenantInitChain);
        this.tenantDataSourceCache = (TenantDataSourceCache) SpringUtil.getBean("tenantDataSourceCache");
        this.tenantDynamicRoutingDataSource = (TenantDynamicRoutingDataSource) SpringUtil.getBean("tenantDynamicRoutingDataSource");
    }

    @Override
    public void handler(Tenant tenant) {
        try {
//        域名做key
            String key = tenant.getDomain();
            String url = tenant.getDbUrl() + tenant.getSimplicity();
            String username = tenant.getDbUser();
            String password = tenant.getDbPass();
            tenantDataSourceCache.createDataSource(key, TenantDataSourceCache.DRIVE_CLASS_NAME, url, username, password);
            ConcurrentMap targetDataSources = TenantDataSourceCache.cache.asMap();
            tenantDynamicRoutingDataSource.setTargetDataSources(targetDataSources);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            如果下一个处理者不是null的话,往下传递执行,如果设定的是中间环节出错则终止处理,则应该将此代码放入try块中
            if (getNextTenantInitChain() != null) {
                getNextTenantInitChain().handler(tenant);
            }
        }
    }
}
