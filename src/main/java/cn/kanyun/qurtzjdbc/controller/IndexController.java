package cn.kanyun.qurtzjdbc.controller;

import lombok.extern.slf4j.Slf4j;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.StringTemplateResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author KANYUN
 */
@Controller
@Slf4j
public class IndexController {

    @GetMapping(value = {"/index", "/"})
    public String index(Model model, HttpServletRequest request) {
//        request.getSession().setAttribute("author", "看雲");
        log.debug("[{}]访问首页", LocalDateTime.now());
//        if (request.getSession().getAttribute("author") == null) {
//            return "redirect:login";
//        }
        request.setAttribute("content","你已成功进入首页");
        return "index";
    }

    /**
     * @param model
     * @param request
     * @return
     * @ResponseStatus注解可以标注请求处理方法。使用此注解，可以指定响应所需要的HTTP STATUS。
     * 特别地，我们可以使用HttpStatus类对该注解的value属性进行赋值
     */
    @GetMapping("/error")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String error(Model model, HttpServletRequest request) {
        return "500";
    }


    public static void main(String[] args) throws IOException {
        StringTemplateResourceLoader resourceLoader = new StringTemplateResourceLoader();
        Configuration cfg = Configuration.defaultConfiguration();
        GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
        Template t = gt.getTemplate("hello,${name}");
        t.binding("name", "beetl");
        String str = t.render();
        System.out.println(str);
    }
}