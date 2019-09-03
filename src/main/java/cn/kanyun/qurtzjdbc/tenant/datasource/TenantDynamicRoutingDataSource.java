package cn.kanyun.qurtzjdbc.tenant.datasource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 动态切换路由
 *
 * @author Kanyun
 */
@Slf4j
public class TenantDynamicRoutingDataSource extends AbstractRoutingDataSource {


    /**
     * 当前的动态数据源集合
     */
    private static Map<Object, Object> tenantTargetDataSources = new HashMap<>();

    /**
     * 清除数据源操作
     */
    public static void clearDataSourceType() {
        log.debug("thread:{},remove,dataSource:{}", Thread.currentThread().getName());
        DynamicDatasourceHolder.remove();
    }

    public static void setDataSourceKey(String dataSource) {
        log.debug("thread:{},set,dataSource:{}", Thread.currentThread().getName(), dataSource);
        DynamicDatasourceHolder.set(dataSource);
    }


    /**
     * 该方法决定了需要使用哪个数据库
     * 必须重写
     *
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        String key = DynamicDatasourceHolder.get();
        log.debug("thread:{},determine,dataSource:{}", Thread.currentThread().getName());
        return key;
    }


    /**
     * 动态数据源发生变化，会调用该方法
     *
     * @param targetDataSources
     */
    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        if (tenantTargetDataSources.size() == 0) {
//            当tenantTargetDataSources的容量为0时,说明是项目刚刚启动
            tenantTargetDataSources = targetDataSources;
        } else {
//            如果中间有数据源的变化,那么在这里判断是新加的数据源,还是修改？还是删除？(修改与删除如何优雅判断？)
//            这里通过Key来判断,key即Map的域名,新加的数据中的key如果在tenantTargetDataSources找不到的话,说明是新加的数据源
            Set set = targetDataSources.keySet();
            Iterator iterator = set.iterator();
            while (iterator.hasNext()) {
//                得到新添加的数据源的key
                Object o = iterator.next();
                if (tenantTargetDataSources.get(o) == null) {
                    tenantTargetDataSources.put(o, targetDataSources.get(o));
                } else {
//                    这里判断是删除,还是修改数据源,如果是修改,那么也是这行代码?如果是删除?那么如何优雅实现？
                    tenantTargetDataSources.put(o, targetDataSources.get(o));
                }
            }
        }
        super.setTargetDataSources(tenantTargetDataSources);
        //重点(刷新resolvedDataSources)
        super.afterPropertiesSet();

    }

}
