package cn.kanyun.qurtzjdbc.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.List;


/**
 * @author KANYUN
 */
@Configuration
@Slf4j
public class DruidDataSourceConfig {

    /**
     * 配置这个属性的意义在于，如果存在多个数据源，监控的时候可以通过名字来区分开来。
     * 如果没有配置，将会生成一个名字，格式是："DataSource-" + System.identityHashCode(this)
     */
    @Value("${spring.datasource.name}")
    private String name;

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    /**
     * 这一项可配可不配，如果不配置druid会根据url自动识别dbType，然后选择相应的driverClassName(建议配置下)
     */
    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    /**
     * 初始化时建立物理连接的个数。初始化发生在显示调用init方法，或者第一次getConnection时
     */
    @Value("${spring.datasource.initialSize}")
    private int initialSize;

    /**
     * 最小连接池数量
     * 如果配置文件没有该值则设置默认值为10
     */
    @Value("${spring.datasource.minIdle:10}")
    private int minIdle;

    /**
     * 最大连接池数量
     */
    @Value("${spring.datasource.maxActive}")
    private int maxActive;

    /**
     * 获取连接时最大等待时间，单位毫秒。配置了maxWait之后，缺省启用公平锁，并发效率会有所下降，
     * 如果需要可以通过配置useUnfairLock属性为true使用非公平锁。
     */
    @Value("${spring.datasource.maxWait}")
    private int maxWait;

    /**
     * 有两个含义：
     * 1) Destroy线程会检测连接的间隔时间2) testWhileIdle的判断依据，详细看testWhileIdle属性的说明
     */
    @Value("${spring.datasource.timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMillis;

    /**
     * 配置一个连接在池中最小生存的时间，单位是毫秒
     */
    @Value("${spring.datasource.minEvictableIdleTimeMillis:30000}")
    private int minEvictableIdleTimeMillis;

    /**
     * 用来检测连接是否有效的sql，要求是一个查询语句。如果validationQuery为null，testOnBorrow、testOnReturn、testWhileIdle都不会其作用。
     */
    @Value("${spring.datasource.validationQuery:SELECT  1  FROM INFORMATION_SCHEMA.SYSTEM_TABLES}")
    private String validationQuery;

    /**
     * 建议配置为true，不影响性能，并且保证安全性。申请连接的时候检测，
     * 如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。
     */
    @Value("${spring.datasource.testWhileIdle:true}")
    private boolean testWhileIdle;

    /**
     * 申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。
     */
    @Value("${spring.datasource.testOnBorrow:false}")
    private boolean testOnBorrow;

    /**
     * 归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能
     */
    @Value("${spring.datasource.testOnReturn:false}")
    private boolean testOnReturn;

    /**
     * 属性类型是字符串，通过别名的方式配置扩展插件，常用的插件有：
     * 监控统计用的filter:stat日志用的filter:log4j防御sql注入的filter:wall
     */
    @Value("${spring.datasource.filters}")
    private String filters;

    /**
     * 超过多少秒算满查询
     */
    @Value("${spring.datasource.slowSqlMillis:1000}")
    private String slowSqlMillis;

    /**
     * 是否显示慢查询SQL
     */
    @Value("${spring.datasource.logSlowSql:true}")
    private String logSlowSql;

    /**
     * 物理连接初始化的时候执行的sql
     * 原本打算执行：SELECT NOW() FROM DUAL
     * 但是HsqlDB中没有这个表,反正是查询时间,也就可以用下面这种默认的
     */
    @Value("${spring.datasource.connectionInitSqls:VALUES (current_timestamp)}")
    private List<String> connectionInitSqls;


    /**
     * 自动装配时当出现多个Bean候选者时，被注解为@Primary的Bean将作为首选者，否则将抛出异常
     * 默认的DataSource配置在application.yml文件中
     * @return
     */
    @Primary
    @Bean(name = "defaultDataSource")
    public DataSource druidDataSource() {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setName(name);
        datasource.setUrl(dbUrl);
        datasource.setUsername(username);
        datasource.setPassword(password);
        datasource.setDriverClassName(driverClassName);
        datasource.setInitialSize(initialSize);
        datasource.setMinIdle(minIdle);
        datasource.setMaxActive(maxActive);
        datasource.setMaxWait(maxWait);
        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        datasource.setValidationQuery(validationQuery);
        datasource.setTestWhileIdle(testWhileIdle);
        datasource.setTestOnBorrow(testOnBorrow);
        datasource.setTestOnReturn(testOnReturn);
        datasource.setConnectionInitSqls(connectionInitSqls);
//        try {
////            开启Druid防火墙[但是druid不支持Hsql,informix]
//            datasource.setFilters(filters);
//        } catch (SQLException e) {
//            log.error("druid configuration initialization filter", e);
//        }
        return datasource;
    }

    /**
     * web监控,配置
     * @return
     */
    @Bean
    public ServletRegistrationBean druidStatViewServlet() {
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        registrationBean.addUrlMappings("/druid/*");
//        IP白名单 (没有配置或者为空，则允许所有访问)
        registrationBean.addInitParameter("allow", "127.0.0.1");
//        IP黑名单 (存在共同时，deny优先于allow)
        registrationBean.addInitParameter("deny", "");
//        登录Druid控制台的用户名密码
        registrationBean.addInitParameter("loginUsername", "kanyun");
        registrationBean.addInitParameter("loginPassword", "kanyun");
//        禁用HTML页面上的“Reset All”功能
        registrationBean.addInitParameter("resetEnable", "false");
        registrationBean.addInitParameter("slowSqlMillis", slowSqlMillis);
        registrationBean.addInitParameter("logSlowSql", logSlowSql);
        registrationBean.addUrlMappings("/druid/*");
        return registrationBean;
    }


    /**
     *  如果需要监控uri，设置Web关联监控配置
     * @return
     */
    @Bean
    public FilterRegistrationBean druidWebStatViewFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
//        统计哪些URI
        filterRegistrationBean.addInitParameter("urlPatterns", "/*");
//        排除统计干扰 添加不需要忽略的格式信息.
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*");
        filterRegistrationBean.addInitParameter("profileEnable", "true");
//        用于session监控页面的用户名显示 需要登录后主动将username注入到session里
        filterRegistrationBean.addInitParameter("principalSessionName", "username");
//        添加过滤规则
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;

    }
}