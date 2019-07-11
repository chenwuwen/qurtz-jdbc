package cn.kanyun.qurtzjdbc.controller;

import lombok.extern.slf4j.Slf4j;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.StringTemplateResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author KANYUN
 */
@Controller
@Slf4j
public class IndexController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("title", "欢迎使用该项目");
        model.addAttribute("author", "看雲");
        log.debug("[{}]访问首页", LocalDateTime.now());
        return "index";
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