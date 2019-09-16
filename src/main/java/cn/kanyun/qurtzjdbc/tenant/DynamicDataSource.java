package cn.kanyun.qurtzjdbc.tenant;

import java.lang.annotation.*;

/**
 * targetDataSourceSrc 数据源来源
 * @author Kanyun
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface DynamicDataSource {
    String targetDataSourceSrc() default "";
}
