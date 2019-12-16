package cn.kanyun.qurtzjdbc.tenant.aop;

import cn.kanyun.qurtzjdbc.tenant.DynamicDataSource;
import cn.kanyun.qurtzjdbc.tenant.datasource.DynamicDatasourceHolder;
import cn.kanyun.qurtzjdbc.tenant.datasource.TenantDataSourceCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
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
     * @param joinPoint         切点
     * @param dynamicDataSource 注解类
     */
    @Before("pointCut()&&@within(dynamicDataSource)")
    public void doBefore(JoinPoint joinPoint, DynamicDataSource dynamicDataSource) {
        HttpServletRequest servletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        log.info("Enter DataSourceAOP");
        log.info("目标方法名为:{}", joinPoint.getSignature().getName());
        log.info("目标方法所属类的简单类名:{}", joinPoint.getSignature().getDeclaringType().getSimpleName());
        String targetDataSourceSrc = dynamicDataSource.targetDataSourceSrc();
        log.info("目标方法标注的DynamicDataSource注解的值是:{}", targetDataSourceSrc);
//        先查看域名是否在缓存中
        String domain = servletRequest.getServerName();
        if (TenantDataSourceCache.cache.getIfPresent(domain) == null) {
//            如果访问的域名为空,说明该请求不是从浏览器发出的,而是发生了服务间的调用,既然是服务间发生了调用,那么只能从Session拿了
//            当然并不是只有获取域名来判断数据源这一种方法,这也是为什么要获取自定义注解中的值了,通过自定义注解中的值,可以自定义数据源标识的获取方式
//            也可以从Cookie,Header,session[需要注意使用HttpSession保存的话,当域名更改,将会丢失所有保存的键值对]中获取
//            DynamicDataSource注解中如果存在值的话,一般是这种形式 @DynamicDataSource(#header.domain)
//            在这里我为什么不用session呢,因为我测试时,如果请求更改了域名(登录时采用域名登录(并且登录成功设置了session),但是其他接口的请求却使用了其他域名),那么设置的session将会找不到
//            那么当设置成了header之后,我可以在header中去设置domain,这样也可以解决问题了,登录时必须使用域名,登录成功前端保存域名到header中,以后每次请求携带含有域名的header即可
//            在微服务中,我们服务间的调用,也可以通过携带header来解决这个问题,当然保存session的方法也可以,不过就不能直接使用Servlet的session,而是需要使用jwt生成Token,将token设置到请求的header中
//            同时该token也作为session(第三方的session如redis)的key,可以保存用户的信息,同时信息中可以保存域名,跟上述一样了,只不过,这个token的职责就更多了
            if (targetDataSourceSrc.startsWith("#header")) {
                String key = targetDataSourceSrc.substring(8);
                domain = servletRequest.getHeader(key);
            }
        }
        DynamicDatasourceHolder.set(domain);
    }


    /**
     * 执行方法后清除数据源设置
     *
     * @param joinPoint 切点
     */
    @After("pointCut()")
    public void doAfter(JoinPoint joinPoint) {
        DynamicDatasourceHolder.remove();
    }

}
