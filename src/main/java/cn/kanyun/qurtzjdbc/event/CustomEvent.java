package cn.kanyun.qurtzjdbc.event;

import lombok.Data;
import org.springframework.context.ApplicationEvent;

/**
 * 自定义事件类 继承ApplicationEvent
 * 发布事件需要在事件源类中使用ApplicationContext.publishEvent()方法
 * 如：applicationContext.publishEvent(new CustomEvent(this,"CustomEvent事件"));
 * @author Kanyun
 */
@Data
public class CustomEvent extends ApplicationEvent {

    private String name;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public CustomEvent(Object source,String name) {
        super(source);
        this.name = name;
    }
}
