package cn.kanyun.qurtzjdbc.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import io.swagger.annotations.ApiOperation;
/**
 * swagger配置类
 * 通过@Configuration注解，让Spring来加载该类配置
 * 再通过@EnableSwagger2注解（加在springboot的启动类上）来启用Swagger2
 * @author Kanyun
 */
@Configuration
@ComponentScan(basePackages = {"cn.kanyun.qurtzjdbc.controller","cn.kanyun.qurtzjdbc.tenant.controller"})
@ConditionalOnProperty(prefix = "qurtz_jdbc", name = "swagger-open", havingValue = "true")
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
//                这里采用包含注解的方式来确定要显示的接口
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
//                这里采用包扫描的方式来确定要显示的接口
                .apis(RequestHandlerSelectors.basePackage("cn.kanyun.qurtzjdbc.controller"))
                .apis(RequestHandlerSelectors.basePackage("cn.kanyun.qurtzjdbc.tenant.controller"))
//               可以根据url路径设置哪些请求加入文档，忽略哪些请求
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 创建该API的基本信息（这些基本信息会展现在文档页面中）
     * 访问地址：http://项目实际地址/swagger-ui.html
     * 但是由于原版springfox-swagger-ui界面太丑,因此换成swagger-ui-layer
     * 访问地址变为http://项目实际地址/docs.html
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
//                设置文档的标题
                .title("Qurtz_Jdbc Doc")
//                设置文档的描述
                .description("Qurtz_Jdbc Api文档")
                .termsOfServiceUrl("http://localhost/")
                .contact(new Contact("kanyun", "https://github.com/chenwuwen/qurtz-jdbc", ""))
                .version("2.0")
                .build();
    }

}
