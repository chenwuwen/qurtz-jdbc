package cn.kanyun.qurtzjdbc.listener;

import cn.kanyun.qurtzjdbc.event.CustomEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 对于 Spring 容器的一些事件，可以监听并且触发相应的方法。通常的方法有 2 种，
 * ApplicationListener 接口和@EventListener 注解
 * 使用@EventListener 注解，实现对任意的方法都能监听事件
 * 在任意方法上标注@EventListener 注解，指定 classes，即需要处理的事件类型，一般就是 ApplicationEven 及其子类，可以设置多项
 * 需要注意的是如果监听自定义的事件,这里@EventListener中的class的值为 发布自定义事件的类
 * 同时发布事件的类要继承ApplicationEven,发布事件时要调用applicationContext.publishEvent方法
 * https://www.jianshu.com/p/e2d257ce410d?from=timeline&isappinstalled=0
 *  @author Kanyun
 */
@Slf4j
@Component
public class CustomListener {

    /**
     * 对于要监听事件的处理,对于多个事件的监听
     * 可以定义多个重载的方法,只需要方法中的参数(事件类型)不同即可
     * @param event 要监听的事件类,其需要继承ApplicationEvent
     */
    @EventListener
    public void exec(CustomEvent event) {
        log.info(event.getName());
    }
}
