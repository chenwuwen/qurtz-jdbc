package cn.kanyun.qurtzjdbc.config;

import cn.kanyun.qurtzjdbc.tenant.datasource.TenantDynamicRoutingDataSource;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.util.DruidDataSourceUtils;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * MybatisPlus 配置
 *
 * @author KANYUN
 */
@EnableTransactionManagement
@Configuration
@MapperScan(value = {"cn.kanyun.qurtzjdbc.dao", "cn.kanyun.qurtzjdbc.tenant.mapper"})
public class MybatisPlusConfig {

    /**
     * 默认的DataSource
     * 定义在DruidDataSourceConfig中
     */
    @Resource
    private DataSource defaultDataSource;

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }


    /**
     * 注册动态数据源
     *
     * @return
     */
    @Bean("tenantDynamicRoutingDataSource")
    public DataSource dynamicDataSource() {
        TenantDynamicRoutingDataSource dynamicRoutingDataSource = new TenantDynamicRoutingDataSource();
//        这里初始化了长度,如果yml配置文件中配置了多个数据库,则配置对应容量
        Map<Object, Object> dataSourceMap = new HashMap<>(1);
        dataSourceMap.put("defaultDataSource", defaultDataSource);
        // 设置默认数据源
        dynamicRoutingDataSource.setDefaultTargetDataSource(defaultDataSource);
        dynamicRoutingDataSource.setTargetDataSources(dataSourceMap);
        return dynamicRoutingDataSource;
    }

    /**
     * 事务管理器
     *
     * @return the platform transaction manager
     */
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dynamicDataSource());
    }

    /**
     * @return
     * @ConfigurationProperties注解也可以读取yml文件 多数据源要配置SqlSessionFactoryBean否则无法切换数据源
     * 同时这里需要使用MybatisSqlSessionFactoryBean 而不是SqlSessionFactoryBean
     * 否则会报错：Invalid bound statement (not found)
     * 如果不是使用的不是Mybatis-Plus的话,使用SqlSessionFactoryBean
     */
    @Bean
    @ConfigurationProperties(prefix = "mybatis-plus")
    public MybatisSqlSessionFactoryBean sqlSessionFactoryBean() throws IOException {
        MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
//        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        // 配置数据源，此处配置为关键配置，如果没有将 dynamicDataSource 作为数据源则不能实现切换
        sqlSessionFactoryBean.setDataSource(dynamicDataSource());
        return sqlSessionFactoryBean;
    }
}