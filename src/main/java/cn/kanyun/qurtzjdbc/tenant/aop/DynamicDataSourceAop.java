package cn.kanyun.qurtzjdbc.tenant.aop;

import cn.kanyun.qurtzjdbc.tenant.datasource.DynamicDatasourceHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Kanyun
 */
@Slf4j
@Order(-1)
@Aspect
@Component
public class DynamicDataSourceAop {

    /**
     * 定义切点,来拦截注解，需要注意的是@annotation是用来拦截方法的,当注解标记在类或者接口上时
     * 使用AOP并不能拦截,针对类的拦截需要使用@within，如下
     */

//    @Pointcut("@annotation(cn.kanyun.qurtzjdbc.tenant.DynamicDataSource)")
    @Pointcut("@within(cn.kanyun.qurtzjdbc.tenant.DynamicDataSource)")
    public void pointCut() {
    }

    /**
     * 执行方法前更换数据源
     *
     * @param joinPoint 切点
     */
    @Before("pointCut()")
    public void doBefore(JoinPoint joinPoint) {
        HttpServletRequest servletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        log.info("Enter DataSourceAOP");
        String domain = servletRequest.getServerName();
        DynamicDatasourceHolder.set(domain);
    }

    /**
     * 执行方法后清除数据源设置
     *
     * @param joinPoint 切点
     */
    @After("pointCut()")
    public void doAfter(JoinPoint joinPoint) {
//        DynamicDatasourceHolder.remove();
    }

}
