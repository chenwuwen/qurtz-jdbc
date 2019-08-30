package cn.kanyun.qurtzjdbc.common;

/**
 * @ConditionalOnClass与@ConditionalOnMissingClass
 * 这两个注解属于类条件注解，它们根据是否存在某个类作为判断依据来决定是否要执行某些配置。
 * @ConditionalOnBean与@ConditionalOnMissingBean
 * 这两个注解属于对象条件注解，根据是否存在某个对象作为依据来决定是否要执行某些配置方法
 * @ConditionalOnProperty注解
 * 会根据Spring配置文件中的配置项是否满足配置要求，从而决定是否要执行被其标注的方法
 * @ConditionalOnResource注解
 * 用于检测当某个配置文件存在使，则触发被其标注的方法
 * @ConditionalOnWebApplication与@ConditionalOnNotWebApplication
 * 这两个注解用于判断当前的应用程序是否是Web应用程序。如果当前应用是Web应用程序，
 * 则使用Spring WebApplicationContext,并定义其会话的生命周期
 * @ConditionalExpression
 * 此注解可以让我们控制更细粒度的基于表达式的配置条件限制。当表达式满足某个条件或者表达式为真的时候，
 * 将会执行被此注解标注的方法
 * @Conditional注解
 * 可以控制更为复杂的配置条件。在Spring内置的条件控制注解不满足应用需求的时候，可以使用此注解定义自定义的控制条件，以达到自定义的要求
 */
public class CheckCondition {
}
