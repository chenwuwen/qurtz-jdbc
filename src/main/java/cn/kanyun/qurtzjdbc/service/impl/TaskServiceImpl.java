package cn.kanyun.qurtzjdbc.service.impl;

import cn.kanyun.qurtzjdbc.entity.JobEntity;
import cn.kanyun.qurtzjdbc.quartz.QuartzService;
import cn.kanyun.qurtzjdbc.service.TaskService;
import cn.kanyun.qurtzjdbc.util.JavaFileUtil;
import cn.kanyun.qurtzjdbc.util.Reflections;
import com.google.common.reflect.Invokable;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import javax.annotation.Resource;
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

    /**
     * 注入QuartzService
     */
    @Resource
    private QuartzService quartzService;

    @Override
    public boolean isJavaFile(String filePath) throws FileNotFoundException {
        return JavaFileUtil.isJavaFile(filePath);
    }

    @Override
    public boolean isStander(Object o) {

//        判断是否是Job的子类
        if (o instanceof org.quartz.Job) {
//            spring的反射类ReflectionUtils 查找对象中是否有execute(JobExecutionContext jobExecutionContext)方法
            Method method = ReflectionUtils.findMethod(o.getClass(), "execute", JobExecutionContext.class);
            if (method != null) {
//                判断方法是否是Public,使用JDK的API
//                Modifier.isPublic(method.getModifiers());

                Invokable invokable = Invokable.from(method);
//                判断方法是否是Public,使用Guava的API
//                invokable.isPublic();
//                invokable.getReturnType();
//                要保证类中的方法是否合规,一般判断返回值,入参,修饰符,但其实判断那么多没什么用,只要这个方法是有@Override注解就可以
                Annotation[] annotations = invokable.getAnnotations();
//                由于Override注解只存在在源码期,在编译的时候,就已经去掉了,因此不能判断是否有Override注解,上面的判断就已经足够了
//                if (Arrays.asList(annotations).contains(Override.class)) {
//                    return true;
//                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void insertJob(JobEntity entity) throws ClassNotFoundException, InstantiationException, SchedulerException, IllegalAccessException {
        quartzService.addJob(entity);
    }
}
