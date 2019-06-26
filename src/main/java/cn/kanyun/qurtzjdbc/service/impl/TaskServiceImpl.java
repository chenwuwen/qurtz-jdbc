package cn.kanyun.qurtzjdbc.service.impl;

import cn.kanyun.qurtzjdbc.service.TaskService;
import cn.kanyun.qurtzjdbc.util.JavaFileUtil;
import cn.kanyun.qurtzjdbc.util.Reflections;
import com.google.common.reflect.Invokable;
import org.quartz.Job;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.io.FileNotFoundException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * @author Kanyun
 */
@Service("taskServiceImpl")
public class TaskServiceImpl implements TaskService {
    @Override
    public boolean isJavaFile(String filePath) throws FileNotFoundException {
        JavaFileUtil.isJavaFile(filePath);
        return false;
    }

    @Override
    public boolean isStander(Object o) {

//        判断是否是Job的子类
        if (o instanceof org.quartz.Job) {
//            spring的反射类ReflectionUtils 查找对象中是否有execute方法
            Method method = ReflectionUtils.findMethod(o.getClass(), "execute");
            if (method != null) {
//                判断方法是否是Public,使用JDK的API
//                Modifier.isPublic(method.getModifiers());

//                判断方法是否是Public,使用Guava的API
                Invokable invokable = Invokable.from(method);
//                invokable.isPublic();
//                invokable.getReturnType();
//                要保证类中的方法是否合规,一般判断返回值,入参,修饰符,但其实判断那么多没什么用,只要这个方法是有@Override注解就可以
                Annotation[] annotations = invokable.getAnnotations();
                if (Arrays.asList(annotations).contains(Override.class)) {
                    return true;
                }
            }
        }
        return false;
    }
}
