package cn.kanyun.qurtzjdbc.tenant;

import cn.kanyun.qurtzjdbc.entity.Tenant;
import cn.kanyun.qurtzjdbc.graphql.exec.TenantHandler;
import cn.kanyun.qurtzjdbc.tenant.chain.BuildAppChain;
import cn.kanyun.qurtzjdbc.tenant.chain.CreateDatabaseChain;
import cn.kanyun.qurtzjdbc.tenant.chain.InitTenantTableChain;
import cn.kanyun.qurtzjdbc.tenant.chain.UpdateDynamicDataSourceChain;
import cn.kanyun.qurtzjdbc.tenant.datasource.TenantDynamicRoutingDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Observable;
import java.util.Observer;

/**
 * 租户(Tenant)观察者
 * Observable可以主动推（push）数据给观察者对象，也可以让观察者对象拉（pull）数据。
 *
 * @author KANYUN
 */
@Slf4j
@Component
public class TenantObserver implements Observer {

    /**
     * 被观察者(主题)
     */
    private Observable observable;

    @Resource
    private TenantDynamicRoutingDataSource tenantDynamicRoutingDataSource;


    /**
     * 构造函数,将自己的实例添加进观察者列表
     * @param observable
     */
    public TenantObserver(Observable observable) {
        this.observable = observable;
        observable.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof TenantHandler) {
            log.debug("观察到Tenant发生变化");
            Tenant tenant = ((TenantHandler) o).getTenant();
            log.debug("发生变化的数据：{}", tenant.toString());
            UpdateDynamicDataSourceChain updateDynamicDataSourceChain = new UpdateDynamicDataSourceChain(null);
            BuildAppChain buildAppChain = new BuildAppChain(updateDynamicDataSourceChain);
            InitTenantTableChain initTenantTableChain = new InitTenantTableChain(buildAppChain);
            CreateDatabaseChain createDatabaseChain = new CreateDatabaseChain(initTenantTableChain);
            createDatabaseChain.handler(tenant);
            initTenantTableChain.handler(tenant);
            buildAppChain.handler(tenant);
            updateDynamicDataSourceChain.handler(tenant);

//        这里不再执行tenantDataSource.createDataSource()方法是因为tenantDataSource本身也是一个观察者
//        当Tenant发生改变时,就已经执行了createDataSource()方法,
//        那为什么更新动态数据源的操作不放到tenantDataSource呢,因为那个时候数据库还没有创建好,先将租户初始化链完成,再进行更新
//        tenantDataSource.createDataSource(key, TenantDataSource.DRIVE_CLASS_NAME, url, username, password);
//            ConcurrentMap targetDataSources = TenantDataSourceC.cache.asMap();
//            tenantDynamicRoutingDataSource.setTargetDataSources(targetDataSources);
        }
    }
}