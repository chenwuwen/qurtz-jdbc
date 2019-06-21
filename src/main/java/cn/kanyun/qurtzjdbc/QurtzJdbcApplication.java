package cn.kanyun.qurtzjdbc;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Kanyun
 */
@SpringBootApplication
public class QurtzJdbcApplication {

	public static void main(String[] args) {
//		SpringApplication.run(QurtzJdbcApplication.class, args);
		SpringApplication application = new SpringApplication(QurtzJdbcApplication.class);
//		如果不想看到任何的banner，可以将其关闭，当然也可以自己自定义banner
		application.setBannerMode(Banner.Mode.OFF);
		application.run(args);
	}

}
