package cn.kanyun.qurtzjdbc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @EnableAutoConfiguration注解用于通知Spring，根据当前类路径下引入的依赖包，自动配置与这些依赖包相关的配置项。
 * @EnableCaching 开启缓存注解
 * @EnableScheduling 开启计划任务支持
 * @ImportResource springboot就会去加载kaptcha.xml文件 ,使用时查看kaptcha.xml文件中Bean Id直接注入使用 https://www.jianshu.com/p/1f2f7c47e812
 * @EnableSwagger2 启用Swagger2
 * @author Kanyun
 */
@Slf4j
@EnableSwagger2
@ImportResource(locations = {"classpath:kaptcha.xml"})
@EnableScheduling
@EnableCaching
//@SpringBootApplication(exclude = {WebMvcAutoConfiguration.class})
@SpringBootApplication
public class QurtzJdbcApplication {

	public static void main(String[] args) {
//		SpringApplication.run(QurtzJdbcApplication.class, args);
		SpringApplication application = new SpringApplication(QurtzJdbcApplication.class);
//		如果不想看到任何的banner，可以将其关闭，当然也可以自己自定义banner
		application.setBannerMode(Banner.Mode.OFF);
		application.run(args);
		log.info(QurtzJdbcApplication.class.getSimpleName() + " start success!");
	}

}
