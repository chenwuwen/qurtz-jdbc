package cn.kanyun.qurtzjdbc.config;

import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.ext.web.WebRenderExt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * beetl扩展属性
 * @author Kanyun
 */
public class GlobalBeetlExt implements WebRenderExt {
    static long version = System.currentTimeMillis();

    @Override
    public void modify(Template template, GroupTemplate gt, HttpServletRequest request, HttpServletResponse response) {
//        js,css 的版本编号，这样，每次在模板里都可以访问变量sysVersion了，不需要在controller里设置，或者通过servlet filter来设置
        template.binding("sysVersion", version);
    }
}
