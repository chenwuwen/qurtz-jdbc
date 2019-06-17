package cn.kanyun.qurtzjdbc.listener;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * SpringBoot中CommandLineRunner的作用
 * 平常开发中有可能需要实现在项目启动后执行的功能，SpringBoot提供的一种简单的实现方案就是
 * 添加一个model并实现CommandLineRunner接口，实现功能的代码放在实现的run方法中(在spring中可以通过实现InitializingBean接口,实现afterPropertiesSet()方法,来实现类似效果,或者实现ApplicationListener)
 * 如果需要按照一定的顺序去执行，那么就需要在实体类上使用一个@Order注解（或者实现Order接口）来表明顺序
 * 在整个应用生命周期内只会执行一次。
 * https://www.cnblogs.com/chenpi/p/9696310.html
 * @author Kanyun
 * @date 2019/6/17
 */
@Component
@Order(value = 10)
public class ScheduleJobInitListener implements CommandLineRunner {
    @Override
    public void run(String... strings) throws Exception {

    }
}
