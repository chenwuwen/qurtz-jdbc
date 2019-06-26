package cn.kanyun.qurtzjdbc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Conditional;

/**
 * Web程序启动类
 * 如果使用外置tomcat部署的话，需要此类，因为外部容器部署的话，就不能依赖于Application的main函数了，
 * 而是要以类似于web.xml文件配置的方式来启动Spring应用上下文，此时我们需要在启动类中继承
 * SpringBootServletInitializer并实现configure方法
 *
 * 这个类的作用与在web.xml中配置负责初始化Spring应用上下文的监听器作用类似，只不过在这里不需要编写额外的XML文件了
 *
 * 我们使用Springboot时,默认是没有webapp这个包的,因为springboot把resource目录当做静态文件配置目录,
 * 所以当没有webapp目录时,也就没有了WEB-INF/web.xml文件,虽然我们可以在maven或者gradle中配置打包成war包
 * 但如果WAR文件里没有启用Spring MVC DispatcherServlet 的web.xml文件或者Servlet初始化类，这个WAR文件就一无是处。
 * 因此Spring Boot提供的SpringBootServletInitializer是一个支持 Spring Boot的Spring WebApplicationInitializer实现。
 * 除了配置Spring的Dispatcher- Servlet，SpringBootServletInitializer还会在Spring应用程序上下文里查找Filter、 Servlet或ServletContextInitializer类型的Bean，
 * 把它们绑定到Servlet容器里。要使用SpringBootServletInitializer，只需创建一个子类，覆盖configure()方法 来指定Spring配置类。
 * @author Kanyun
 */

/**
 * @Conditional注解可以根据自定义条件，选择符合条件的bean到IOC容器中。此注解被用来修饰配置类及其类中的方法。
 * 1.修饰配置类时，表示配置类满足@Conditional注解的自定义条件时，配置类才会被容器加载，获取所需的bean，加入容器。
 * 2.修饰配置类中的方法时：当配置类满足条件，配置类中方法若被@Conditional 注解修饰，满足注解的条件，执行此方法，否则不执行。
 * 注解中条件设置通过其属性值xxx.java定义，xxx.java实现Conditon接口
 */
//@Slf4j
//@Conditional()
//public class QurtzJdbcServletInitializer extends SpringBootServletInitializer {
//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//        log.info("==========以web方式启动应用==========");
//        return builder.sources(QurtzJdbcServletInitializer.class);
//    }
//}
