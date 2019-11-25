package cn.kanyun.qurtzjdbc.config;

import cn.kanyun.qurtzjdbc.interceptor.BaseInterceptor;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.Validator;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 默认情况下，Spring Boot将从类路径或根目录中的
 * /static（ /public或/resources或/META-INF/resources）找静态内容
 * 如果要自定义静态文件位置,重写addResourceHandlers方法
 *
 * @author KANYUN
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {


    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {

    }

    /**
     * 配置内容裁决的一些选项
     *
     * @param configurer
     */
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {

    }

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {

    }

    /**
     * 默认静态资源处理器
     *
     * @param configurer
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {

    }

    /**
     * 拦截器配置
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //需要配置2：----------- 告知拦截器：/static/admin/** 与 /static/user/** 不需要拦截 （配置的是 路径）
        registry.addInterceptor(new BaseInterceptor()).addPathPatterns("/**")
                .excludePathPatterns("/assets/**")
                .excludePathPatterns("/webjars/**");
    }

    /**
     * 静态资源处理【静态资源映射】
     * <p>
     * 添加处理程序以服务静态资源，如图像、JS和CSS
     * 来自Web应用程序根目录下特定位置的文件，以及其他。
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        第一个方法设置访问路径前缀，第二个方法设置资源路径
//        swagger静态资源映射
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
//        webjars 静态资源映射(新增 resourceChain 配置即开启缓存配置,不加这个配置,添加了webjars-locator依赖也不会生效)
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/")
//              生产时建议开启缓存（只是缓存了资源路径而不是资源内容）,开发是可以设置为false,但必须设置resourceChain,否则不生效
                .resourceChain(false);
//        graphql测试客户端graphiql的静态资源映射
        registry.addResourceHandler("/vendor/**").addResourceLocations("classpath:/static/vendor/");

//        本应用(由于使用了gradle的sourceSets所以在打包时会把webapp下的静态页打包到压缩包中)
        registry.addResourceHandler("/assets/**").addResourceLocations("classpath:/assets/");
    }

    /**
     * 跨域CORS配置
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/cors/**")
                .allowedHeaders("*")
                .allowedMethods("POST", "GET")
                .allowedOrigins("*");
    }

    /**
     * 视图跳转控制器
     *
     * 在项目开发过程中，经常会涉及页面跳转问题，而且这个页面跳转没有任何业务逻辑过程，
     * 只是单纯的路由过程 ( 点击一个按钮跳转到一个页面 )，如果项目中有很多类似的无业务逻辑跳转过程，那样会有很多类似的代码
     *  Spring MVC 中提供了一个方法，可以把类似代码统一管理，减少类似代码的书写（根据项目要求，或者代码规范，不一定非要统一管理页面跳转，有时会把相同业务逻辑的代码放在一个类中）
     *
     * addViewControllers可以方便的实现一个请求直接映射成视图，而无需书写controller
     * registry.addViewController("请求路径").setViewName("请求页面文件路径")
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
//        可配置多个请求映射,在不需要业务逻辑情况下避免写许多无所谓的Controller
//        registry.addViewController("请求路径").setViewName("请求页面文件路径")
        registry.addViewController("/error").setViewName("/pages/500.html");
    }

    /**
     * 配置视图解析器
     * ViewResolverRegistry 是一个注册器，用来注册你想自定义的视图解析器等
     *
     * @param registry
     */
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {

    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {

    }

    /**
     * 消息内容转换配置
     * 配置fastJson返回json转换
     *
     * @param converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        创建fastJson消息转换器
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
//        创建配置类
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
//        修改配置返回内容的过滤
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullStringAsEmpty
        );
        fastConverter.setFastJsonConfig(fastJsonConfig);
        //将fastJson添加到视图消息转换器列表内
        converters.add(fastConverter);

    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

    }

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {

    }

    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {

    }

    @Override
    public Validator getValidator() {
        return null;
    }

    @Override
    public MessageCodesResolver getMessageCodesResolver() {
        return null;
    }
}