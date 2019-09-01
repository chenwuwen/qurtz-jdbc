package cn.kanyun.qurtzjdbc.common;

import cn.kanyun.qurtzjdbc.common.tenant.BuildAppChain;
import cn.kanyun.qurtzjdbc.common.tenant.CreateDatabaseChain;
import cn.kanyun.qurtzjdbc.common.tenant.InitTenantTableChain;
import cn.kanyun.qurtzjdbc.entity.Tenant;
import cn.kanyun.qurtzjdbc.graphql.exec.TenantHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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

    private Tenant tenant;

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
            BuildAppChain buildAppChain = new BuildAppChain(null);
            InitTenantTableChain initTenantTableChain = new InitTenantTableChain(buildAppChain);
            CreateDatabaseChain createDatabaseChain = new CreateDatabaseChain(initTenantTableChain);
            createDatabaseChain.handler(tenant);
            initTenantTableChain.handler(tenant);
            buildAppChain.handler(tenant);
        }
    }
}