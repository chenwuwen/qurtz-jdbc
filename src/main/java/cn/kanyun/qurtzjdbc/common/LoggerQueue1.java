package cn.kanyun.qurtzjdbc.common;

import cn.kanyun.qurtzjdbc.entity.LoggerMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Web日志队列
 */
@Slf4j
public class LoggerQueue1 {
    /**
     * 队列大小
     */
    public static final int QUEUE_MAX_SIZE = 10000;

    /**
     * 阻塞队列
     */
    private final BlockingQueue<LoggerMessage> blockingQueue = new LinkedBlockingQueue<>(QUEUE_MAX_SIZE);

    private LoggerQueue1() {
    }

    public static LoggerQueue getInstance() {
        return SingleHolder.INSTANCE.getInstance();
    }

    /**
     * 消息入队
     * 如果入队失败,则移除队头元素,再入队
     *
     * @param loggerMessage
     * @return
     */
    public boolean push(LoggerMessage loggerMessage) {
//        offer: 将指定元素插入此队列中（如果立即可行且不会违反容量限制）,成功时返回 true，如果当前没有可用的空间，则返回 false，不会抛异常
        if (!blockingQueue.offer(loggerMessage)) {
            log.debug("=======LoggerQueue 队列已满,移除旧日志,插入新日志=========");
            blockingQueue.poll();
            return blockingQueue.offer(loggerMessage);
        }
        return true;
    }


    /**
     * 消息出队
     *
     * @return
     */
    public LoggerMessage poll() {
        LoggerMessage message = null;

//            take():取走BlockingQueue里排在首位的对象,若BlockingQueue为空,阻断进入等待状态直到Blocking有新的对象被加入为止
//            message = this.blockingQueue.take();
//            poll(time):取走BlockingQueue里排在首位的对象,若不能立即取出,则可以等time参数规定的时间,取不到时返回null
        message = blockingQueue.poll();

        return message;
    }


    /**
     * 单例模式 -> 静态内部类
     */
//    private static class SingleHolder {
//        private static LoggerQueue INSTANCE = new LoggerQueue();
//    }


    /**
     * 枚举实现的单例模式
     */
    enum SingleHolder {
        INSTANCE;
        LoggerQueue loggerQueue;

        SingleHolder() {
//            loggerQueue = new LoggerQueue();
        }

        private LoggerQueue getInstance() {
            return loggerQueue;
        }

    }

}

