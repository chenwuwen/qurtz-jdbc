package cn.kanyun.qurtzjdbc.common;

import cn.kanyun.qurtzjdbc.entity.LoggerMessage;
import lombok.extern.slf4j.Slf4j;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Web日志队列
 * 使用枚举实现的单例模式，不但可以防止利用反射强行构建单例对象，而且可以在枚举类对象被反序列化的时候，
 * 保证反序列的返回结果是同一对象。
 * 对于其他方式实现的单例模式，如果既想要做到可序列化，又想要反序列化为同一对象，则必须实现readResolve方法。
 * 当然即使是使用了枚举实现单例,也不是万无一失的,枚举实现的单例模式也是会被破坏的：
 * https://blog.csdn.net/kanyun123/article/details/104047715
 */
@Slf4j
public enum LoggerQueue implements Serializable {


    INSTANCE;

    /**
     * 队列大小
     * 这个队列大小不宜太大,否则当在前端连接SSE时,想要查看当前的日志,需要等待比较久的时间
     */
    public static final int QUEUE_MAX_SIZE = 100;

    /**
     * 阻塞队列
     */
    private BlockingQueue<LoggerMessage> blockingQueue;

    /**
     * 可以省略此方法，通过LoggerQueue.INSTANCE进行操
     *
     * @return
     */
    public static LoggerQueue getInstance() {
        return INSTANCE;
    }

    /**
     * 自定义构造方法,用于构造blockingQueue象
     * 枚举的构造方法都是私有的
     */
    LoggerQueue() {
        ClassLoader classLoader = this.getClass().getClassLoader();
        blockingQueue = new LinkedBlockingQueue<>(QUEUE_MAX_SIZE);
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
//            take():取走BlockingQueue里排在首位的对象,若BlockingQueue为空,阻断进入等待状态直到Blocking有新的对象被加入为止
//            message = this.blockingQueue.take();
//            poll(time):取走BlockingQueue里排在首位的对象,若不能立即取出,则可以等time参数规定的时间,取不到时返回null
        LoggerMessage message = blockingQueue.poll();

        return message;
    }

}

