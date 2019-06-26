package cn.kanyun.qurtzjdbc.config;

import lombok.extern.slf4j.Slf4j;
import org.beetl.core.GroupTemplate;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.beetl.core.resource.WebAppResourceLoader;
import org.beetl.ext.spring.BeetlGroupUtilConfiguration;
import org.beetl.ext.spring.BeetlSpringViewResolver;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;

import java.io.File;

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
        ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader();
//        WebAppResourceLoader 是用于Java EE web应用的资源模板加载器，默认根路径是WebRoot目录。也可以通过制定root属性来设置相对于WebRoot的的模板根路径
//        WebAppResourceLoader 假定 beetl.jar 是位于 WEB-INF/lib 目录下，因此，可以通过WebAppResourceLoader类的路径来推断出WebRoot路径从而指定模板根路径。所有线上环境一般都是如此，如果是开发环境或者其他环境不符合此假设，你需要调用resourceLoader.setRoot() 来指定模板更路径
//        WebAppResourceLoader resourceLoader = new WebAppResourceLoader();
        System.out.println(resourceLoader.getRoot());
        beetlGroupUtilConfiguration.setResourceLoader(resourceLoader);
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
        GroupTemplate groupTemplate = beetlGroupUtilConfiguration.getGroupTemplate();
        groupTemplate.setClassLoader(loader);

//        这里是需要注册的标签
//        如果需要自定义标签,需要在此处注册,同时注意获取GroupTemplate前必须先调用init方法，否则获取到的GroupTemplate为 null,同时不要在@Bean中指定initMethod = "init"
//        groupTemplate.registerTag("simpleTag", SimpleHtmlTag.class);
        return beetlGroupUtilConfiguration;
    }

    @Bean(name = "beetlViewResolver")
    public BeetlSpringViewResolver getBeetlSpringViewResolver(@Qualifier("beetlConf") BeetlGroupUtilConfiguration beetlGroupUtilConfiguration) {
        BeetlSpringViewResolver beetlSpringViewResolver = new BeetlSpringViewResolver();
//      不启用 springMvc 的 前缀。。使用 beetl自带 的 RESOURCE.root
//        beetlSpringViewResolver.setPrefix("view/");
//        beetl默认处理后缀名为btl的文件,这里不进行设置时,在Controller中返回页面时应该带上以btl为后缀结尾的页面
//        beetl.suffix 默认为btl，表示只处理视图后缀为btl的模板，比如controller里代码是“return /common/index.btl”,则能被Beetl处理，你写成"return /common/index",或者"/common/index.html",都会出现404错误
//        beetlSpringViewResolver.setSuffix(".btl");
        beetlSpringViewResolver.setContentType("text/html;charset=UTF-8");
//        viewResolver 作用的顺序
        beetlSpringViewResolver.setOrder(0);
//        解析器里属性viewNames，这个用于判断controller返回的path到底应该交给哪个视图解析器来做,比如返回 aa/xx 由a解析器解析 返回 bb/xx 则由b解析器解析
        beetlSpringViewResolver.setViewNames("/");
        beetlSpringViewResolver.setConfig(beetlGroupUtilConfiguration);
        return beetlSpringViewResolver;
    }

    /**
     * Beetl的核心是GroupTemplate，创建GroupTemplate需要俩个参数，一个是模板资源加载器，一个是配置类
     * 模板资源加载器Beetl内置了6种:
     * StringTemplateResourceLoader：字符串模板加载器，用于加载字符串模板
     * FileResourceLoader：文件模板加载器，需要一个根目录作为参数构造，，传入getTemplate方法的String是模板文件相对于Root目录的相对路径
     * ClasspathResourceLoader：文件模板加载器，模板文件位于Classpath里
     * WebAppResourceLoader：用于webapp集成，假定模板根目录就是WebRoot目录
     * MapResourceLoader : 可以动态存入模板
     * CompositeResourceLoader 混合使用多种加载方式
     * @param beetlGroupUtilConfiguration
     * @return
     */
//    @Bean(name = "groupTemplate")
//    public GroupTemplate getGroupTemplate(
//            @Qualifier("beetlConf") BeetlGroupUtilConfiguration beetlGroupUtilConfiguration) {
//        return beetlGroupUtilConfiguration.getGroupTemplate();
//
//    }

}