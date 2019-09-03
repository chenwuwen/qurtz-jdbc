package cn.kanyun.qurtzjdbc.tenant.datasource;

/**
 * 通过ThreadLocal维护一个全局唯一的map来实现数据源的动态切换
 *
 * @author Kanyun
 */
public class DynamicDatasourceHolder {

    private static final ThreadLocal<String> DATASOURCE_HOLDER = new ThreadLocal<>();


    public static void set(String datasource) {
        DATASOURCE_HOLDER.set(datasource);
    }

    /**
     * 得到当前的Key,如果当前key为null,则返回默认key
     * 但是如果在AbstractRoutingDataSource的子类中设置setDefaultTargetDataSource()
     * 那么默认将使用这个默认数据库
     * @return
     */
    public static String get() {
//        return DATASOURCE_HOLDER.get() != null ? DATASOURCE_HOLDER.get() : "defaultDataSource";
        return DATASOURCE_HOLDER.get() ;
    }

    public static void remove() {
        DATASOURCE_HOLDER.remove();
    }
}
