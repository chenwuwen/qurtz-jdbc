package cn.kanyun.qurtzjdbc.common;

import org.beetl.ext.spring.BeetlGroupUtilConfiguration;

import java.util.HashMap;
import java.util.Map;

/**
 * beetl全局变量，方便在模板中直接调用
 * 使用时需要在BeetlConfig.java中的getBeetlGroupUtilConfiguration方法中
 * 进行设置，beetlGroupUtilConfiguration.setSharedVars(BeetlGlobalParam.getParam());
 */
public class BeetlGlobalParam {


    public static Map getParam() {

        //全局共享变量
        Map<String, Object> shared = new HashMap<>(1);
        shared.put("title", "欢迎使用该项目");
        return shared;

    }

    public static Map getFunction() {
        //全局共享方法
        Map<String, Object> shared = new HashMap<>(4);
//        shared.put("shiro", new ShiroExt());
//        shared.put("tool", new ToolUtil());
//        shared.put("kaptcha", new KaptchaUtil());
        return shared;
    }
}
