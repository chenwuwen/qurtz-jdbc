package cn.kanyun.qurtzjdbc.tenant.datasource;

import cn.kanyun.qurtzjdbc.entity.Tenant;
import cn.kanyun.qurtzjdbc.graphql.exec.TenantHandler;
import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

/**
 * 租户数据源
 * 同时作为Tenant的观察者,当Tenant发生变化时,更新数据源
 *
 * @author Kanyun
 */
@Slf4j
@Component
public class TenantDataSourceCache {

    /**
     * 动态添加的数据源的 驱动名
     */
    public static final String DRIVE_CLASS_NAME = "com.mysql.jdbc.Driver";

    public static Cache<String, javax.sql.DataSource> cache;

    static {
        cache = CacheBuilder.newBuilder()
                // 设置缓存的最大容量
                .maximumSize(1000)
                // 设置缓存在写入一分钟后失效
                .expireAfterWrite(30, TimeUnit.MINUTES)
                // 设置并发级别为100
                .concurrencyLevel(100)
                .build();
    }

    /**
     * 创建数据源
     *
     * @param key            缓存的Key
     * @param driveClassName 数据库驱动名
     * @param url            数据库连接地址
     * @param username       数据库连接名
     * @param password       数据库连接密码
     */
    public void createDataSource(String key, String driveClassName, String url, String username, String password) {
        try {
//            下面两行代码相当于使用原始的JDBC测试是否连接的上数据库
            Class.forName(driveClassName);
            // 相当于连接数据库
            DriverManager.getConnection(url, username, password);

//            配置数据库连接池
            DruidDataSource druidDataSource = new DruidDataSource();
//            Druid的这个name主要是用来在使用Druid监控时查看各个连接池的使用情况,不设置的话,将会显示为 数据源+index的形式
            druidDataSource.setName(key);
            druidDataSource.setDriverClassName(driveClassName);
            druidDataSource.setUrl(url);
            druidDataSource.setUsername(username);
            druidDataSource.setPassword(password);
            druidDataSource.setMaxWait(60000);
            druidDataSource.setFilters("stat");
            druidDataSource.init();
            cache.put(key, druidDataSource);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
