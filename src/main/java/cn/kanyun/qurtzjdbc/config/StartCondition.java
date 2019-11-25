package cn.kanyun.qurtzjdbc.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 条件类，判断启动环境
 * @author Kanyun
 */
public class StartCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
//        判断是否是linux系统
//        context.getEnvironment().getProperty("os.name").contains("linux");
//        判断是tomcat启动,还是springboot的main方法启动
//        context.getClassLoader().
        return false;
    }
}
