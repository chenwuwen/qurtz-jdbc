package cn.kanyun.qurtzjdbc.filter;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import cn.kanyun.qurtzjdbc.common.LoggerQueue;
import cn.kanyun.qurtzjdbc.entity.LoggerMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * LogBack 日志拦截器
 * 用来在web端输出后台运行日志
 * 在Logback-classic中提供两个类型的 filters , 一种是 regular filters ， 另一种是 turbo filter。
 * regular filters 是与appender 绑定的， 而turbo filter是与与logger context（logger 上下文）绑定的，区别就是，turbo filter过滤所有logging request ，而regular filter只过滤某个appender的logging request。
 * 在 logback-classic中，filters 继承 Filter 抽象类，Filter 抽象类有一个 decide()抽象方法，接收一个 ILoggingEvent 对象参数，
 * 而在 logback-access 中 则是 AccessEvent 对象。该方法返回一个enum类型 FilterReply。值可以是
 * DENY：如果方法返回DENY（拒绝），则跳出过滤链，而该 logging event 也会被抛弃。
 * NRUTRAL：如果返回NRUTRAL（中立），则继续过滤链中的下一个过滤器。
 * ACCEPT：如果返回ACCEPT（通过），则跳出过滤链
 *
 */
@Slf4j
public class WebLogFilter extends Filter<ILoggingEvent> {

    private static final LoggerQueue loggerQueue = LoggerQueue.getInstance();

    @Override
    public FilterReply decide(ILoggingEvent event) {
        LoggerMessage loggerMessage = new LoggerMessage();
        loggerMessage.setBody(event.getMessage());
//        event.getTimeStamp()得到的时间是时间抽 将其转换为 yyyy-MM-dd HH:mm:ss 形式
        loggerMessage.setTimestamp(convertTimeToString(event.getTimeStamp()));
        loggerMessage.setLevel(event.getLevel().levelStr);
        loggerMessage.setThreadName(event.getThreadName());
//        event.getLoggerName()得到的是类的全限定名
        loggerMessage.setClassName(event.getLoggerName());
        //将LoggerMessage 推入队列中去
        loggerQueue.push(loggerMessage);
        return FilterReply.ACCEPT;
    }


    /**
     * 时间转换
     *
     * @param time
     * @return
     */
    private String convertTimeToString(Long time) {
        Assert.notNull(time, "time is null");
        DateTimeFormatter ftf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return ftf.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault()));
    }
}
