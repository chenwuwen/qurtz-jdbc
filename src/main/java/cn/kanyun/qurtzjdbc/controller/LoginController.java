package cn.kanyun.qurtzjdbc.controller;

import cn.kanyun.qurtzjdbc.entity.UserEntity;
import cn.kanyun.qurtzjdbc.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

/**
 * @author HLWK-06
 */
@Controller
public class LoginController {

    @Resource
    private UserService userService;

    @PostMapping("/login")
    public String login(UserEntity user) {
        if (userService.login(user)) {
            return "redirect:/index";
        }
        return "login";
    }

}
