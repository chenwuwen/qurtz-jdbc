package cn.kanyun.qurtzjdbc.config;

import lombok.extern.slf4j.Slf4j;
import org.beetl.core.GroupTemplate;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.beetl.ext.spring.BeetlGroupUtilConfiguration;
import org.beetl.ext.spring.BeetlSpringViewResolver;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;

/**
 * Beetl模板官网：http://ibeetl.com/
 * Beetl与springboot集成http://ibeetl.com/guide/#beetl
 * 使用Starter来配置已经够用，如果你想自己配置模板引擎，
 * 通过java config来配置 beetl需要的BeetlGroupUtilConfiguration，和 BeetlSpringViewResolver
 * @author KANYUN
 */
@Slf4j
@Configuration
public class BeetlConfig {


    @Bean(name = "beetlConf",initMethod = "init")
    public BeetlGroupUtilConfiguration getBeetlGroupUtilConfiguration() {
        BeetlGroupUtilConfiguration beetlGroupUtilConfiguration = new BeetlGroupUtilConfiguration();
        ClasspathResourceLoader classpathResourceLoader = new ClasspathResourceLoader();
        beetlGroupUtilConfiguration.setResourceLoader(classpathResourceLoader);
//        设置 beetl.properties 路径
        ResourcePatternResolver resouce = ResourcePatternUtils.getResourcePatternResolver(new DefaultResourceLoader());
        beetlGroupUtilConfiguration.setConfigFileResource(resouce.getResource("classpath:beetl.properties"));
        beetlGroupUtilConfiguration.init();
        //如果使用了优化编译器，涉及到字节码操作，需要添加ClassLoader
        //获取Spring Boot 的ClassLoader
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = BeetlConfig.class.getClassLoader();
        }
        beetlGroupUtilConfiguration.getGroupTemplate().setClassLoader(loader);

//        如果需要自定义标签,需要在此处注册,同时注意获取GroupTemplate前必须先调用init方法，否则获取到的GroupTemplate为 null,同时不要在@Bean中指定initMethod = "init"
        GroupTemplate groupTemplate = beetlGroupUtilConfiguration.getGroupTemplate();
//        这里是需要注册的标签
//        groupTemplate.registerTag("simpleTag", SimpleHtmlTag.class);
        return beetlGroupUtilConfiguration;
    }

    @Bean(name = "beetlViewResolver")
    public BeetlSpringViewResolver getBeetlSpringViewResolver(@Qualifier("beetlConf") BeetlGroupUtilConfiguration beetlGroupUtilConfiguration) {
        BeetlSpringViewResolver beetlSpringViewResolver = new BeetlSpringViewResolver();
//      不启用 springMvc 的 前缀。。使用 beetl自带 的 RESOURCE.root
//        beetlSpringViewResolver.setPrefix("webapp/");
//        beetl默认处理后缀名为btl的文件,这里不进行设置时,在Controller中返回页面时应该带上以btl为后缀结尾的页面
//        beetl.suffix 默认为btl，表示只处理视图后缀为btl的模板，比如controller里代码是“return /common/index.btl”,则能被Beetl处理，你写成"return /common/index",或者"/common/index.html",都会出现404错误
//        beetlSpringViewResolver.setSuffix(".btl");
        beetlSpringViewResolver.setContentType("text/html;charset=UTF-8");
//        viewResolver 作用的顺序
        beetlSpringViewResolver.setOrder(0);
        beetlSpringViewResolver.setConfig(beetlGroupUtilConfiguration);
        return beetlSpringViewResolver;
    }

    @Bean(name = "groupTemplate")
    public GroupTemplate getGroupTemplate(
            @Qualifier("beetlConf") BeetlGroupUtilConfiguration beetlGroupUtilConfiguration) {
        return beetlGroupUtilConfiguration.getGroupTemplate();

    }

}