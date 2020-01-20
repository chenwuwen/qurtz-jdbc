package cn.kanyun.qurtzjdbc.controller;

import cn.kanyun.qurtzjdbc.common.LoggerQueue;
import cn.kanyun.qurtzjdbc.entity.LoggerMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuples;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/logger")
public class LoggerController {

    private static final LoggerQueue loggerQueue = LoggerQueue.getInstance();

    /**
     * 向web端推送日志
     * Server Send Event
     * SSE–server send event是一种服务端推送的技术 需要设置类型为event-stream
     * 只需要客户端请求一次,如果请求成功(如返回200),那么服务端就会不断向客户端发送数据,其实是关闭了request而没有关闭response
     * 这里返回了SseEmitter,这其实是使用了基于SpringMvc的实现
     * 使用SseEmitter的send()方法发送数据,如果发送的数据为null,前台将无法触发接收数据事件
     * 同时打开浏览器的控制台查看EventStream也没有动静
     * https://mp.weixin.qq.com/s/gACgyBGHNoexKTxkCpD7Ug
     *
     * @return
     */
    @GetMapping(value = "/constantly", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter push() {
        //timeout设置为0表示不超时,如果不设置为0，那么如果SseEmitter在指定的时间（AsyncSupportConfigurer设置的timeout,默认为30秒)未完成会抛出异常
        final SseEmitter emitter = new SseEmitter(0L);
        try {
//            创建定时循环线程池
            ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
//            延迟500毫秒启动,每次间隔500毫秒
            scheduledExecutorService.scheduleAtFixedRate(() -> {
                LoggerMessage loggerMessage = loggerQueue.poll();
                try {
                    emitter.send(loggerMessage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }, 500, 500, TimeUnit.MILLISECONDS);
//            emitter.complete();
        } catch (Exception e) {
            emitter.completeWithError(e);
        }


//        CompletableFuture.runAsync(() -> {
//            try {
//                while (true) {
//                    LoggerMessage loggerMessage = loggerQueue.poll();
//                    emitter.send(loggerMessage);
////                    emitter.complete();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });

        return emitter;
    }


    /**
     * 基于WebFlux实现的SSE
     *
     * @return
     */
//    @GetMapping("/randomNumbers")
//    public Flux<LoggerMessage> randomNumbers() {
//        LoggerMessage message = loggerQueue.poll();
//
////        创建Flux发射器 (1秒产生一个数据)
//        Flux<LoggerMessage> ret = Flux.interval(Duration.ofSeconds(1))
//        Flux<Object> flux = timeFlux.map(seq -> Tuples.of(seq, LoggerMessage))
//                .map(data -> ServerSentEvent.<Integer>builder()
//                        .event("random")
//                        .id(Long.toString(data.getT1()))
//                        .data(data.getT2())
//                        .build());
//        return ret;
//    }
}
