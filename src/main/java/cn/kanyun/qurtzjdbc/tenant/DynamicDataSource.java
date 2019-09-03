package cn.kanyun.qurtzjdbc.tenant;

import java.lang.annotation.*;

/**
 * @author Kanyun
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface DynamicDataSource {
    String targetDataSource() default "";
}
