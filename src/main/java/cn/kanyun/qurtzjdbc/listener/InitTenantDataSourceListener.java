package cn.kanyun.qurtzjdbc.listener;

import cn.kanyun.qurtzjdbc.entity.Tenant;
import cn.kanyun.qurtzjdbc.service.TenantService;
import cn.kanyun.qurtzjdbc.tenant.datasource.TenantDataSourceCache;
import cn.kanyun.qurtzjdbc.tenant.datasource.TenantDynamicRoutingDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

/**
 * Spring 启动监听,初始化 租户数据源
 * ApplicationListener 后的泛型决定了,要监听的类型,也可以自定义类继承ApplicationEvent
 *
 * @author Kanyun
 */
@Slf4j
@Component
public class InitTenantDataSourceListener implements ApplicationListener<ContextRefreshedEvent> {

    @Resource
    private TenantService tenantService;
    @Resource
    private TenantDataSourceCache tenantDataSourceCache;
    @Resource
    private TenantDynamicRoutingDataSource tenantDynamicRoutingDataSource;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        //判断spring容器是否加载完成
        if (event.getApplicationContext().getParent() == null) {
            log.debug("==========Spring启动完成,初始化数据源=========");
            List<Tenant> tenantList = tenantService.queryAll();
            for (Tenant tenant : tenantList) {
//              域名做key
                String key = tenant.getDomain();
                String url = tenant.getDbUrl() + tenant.getSimplicity();
                String username = tenant.getDbUser();
                String password = tenant.getDbPass();
                tenantDataSourceCache.createDataSource(key, TenantDataSourceCache.DRIVE_CLASS_NAME, url, username, password);
            }
            ConcurrentMap targetDataSources = TenantDataSourceCache.cache.asMap();
            tenantDynamicRoutingDataSource.setTargetDataSources(targetDataSources);
        }
    }
}
