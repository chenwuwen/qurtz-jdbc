package cn.kanyun.qurtzjdbc.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.session.StandardManager;
import org.apache.catalina.session.StandardSession;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Spring表达式语言全称为“Spring Expression Language”，缩写为“SpEL”，类似于Struts2x中使用的OGNL表达式语言，
 * 能在运行时构建复杂表达式、存取对象图属性、对象方法调用等等，并且能与Spring功能完美整合，如能用来配置Bean定义
 * SpEL是单独模块，只依赖于core模块，不依赖于其他模块，可以单独使用
 * SpEL在求表达式值时一般分为四步：
 * 1）创建解析器：SpEL使用ExpressionParser接口表示解析器，提供SpelExpressionParser默认实现；
 * 2）解析表达式：使用ExpressionParser的parseExpression来解析相应的表达式为Expression对象。
 * 3）构造上下文：准备比如变量定义等等表达式需要的上下文数据。
 * 4）求值：通过Expression接口的getValue方法根据上下文获得表达式值。
 * 实现SpEL计算接口
 *
 * @author Kanyun
 */
@Component
@Slf4j
public class SpringExpressContext {

    /**
     * EvaluationContext表示上下文对象，用于设置根对象、自定义变量、自定义函数、类型转换器等，
     * 使用setRootObject方法来设置根对象，使用setVariable方法来注册自定义变量，
     * 使用registerFunction来注册自定义函数等等
     * SpEL提供默认实现StandardEvaluationContext；
     */
    private final EvaluationContext context;

    /**
     * 该接口的实例负责解析一个SpEL表达式,返回一个Expression对象
     */
    private final ExpressionParser parser;

    /**
     * ExpressionParser接口：表示解析器，默认实现是SpelExpressionParser类，
     * 使用parseExpression方法将字符串表达式转换为Expression对象，
     * 对于ParserContext接口用于定义字符串表达式是不是模板，及模板开始与结束字符
     */
    private final ParserContext parserContext;

    /**
     * 该接口的实例代表一个表达式,默认实现是SpelExpression类,它包含了一些方法可用于计算,得到表达式的值
     * 其提供getValue方法用于获取表达式值，提供setValue方法用于设置对象值。
     * 其getValue()方法有多个重载,
     */
    private Expression expression;

    private SpringExpressContext() {
        this(null);
    }

    private SpringExpressContext(ApplicationContext applicationContext) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        if (applicationContext != null) {
            context.setBeanResolver(new BeanFactoryResolver(applicationContext));
        }
        SpelParserConfiguration config = new SpelParserConfiguration(true, true);
        this.context = context;
        this.parser = new SpelExpressionParser(config);
        this.parserContext = new TemplateParserContext();
    }

    /**
     * 这个方法参数可以自定义
     *
     * @param target
     * @param args
     * @param result
     */
    public SpringExpressContext(Object target, Object[] args, Object result, Map<String, Object> map) {
        this();
//        往EvaluationContext中放了一个对象,取的时候也需要从这里去取
        Set keys = map.keySet();
        for (Object key : keys) {

//            这里面的判断可以不必使用,但这样的话,当我构造map入参时,该key可能需要设置常量,这样代码维护性就更好
            if (key.equals("session")) {
                HttpSession session = (HttpSession) map.get(key);
                context.setVariable("session", map.get(key));
            }
            if (key.equals("cookie")) {
                context.setVariable("cookie", map.get(key));
            }

        }

    }

    /**
     * 计算表达式的值
     *
     * @param express
     * @return
     */
    public String getValue(String express) {
//        取数据时,从EvaluationContext上下文中去取
        expression = parser.parseExpression(express, parserContext);
        return expression.getValue(context, String.class);
    }


}
