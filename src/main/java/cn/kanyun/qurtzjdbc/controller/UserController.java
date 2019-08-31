package cn.kanyun.qurtzjdbc.controller;

import cn.kanyun.qurtzjdbc.service.UserService;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * @author Kanyun
 */
@Controller
public class UserController {

    @Resource
    private UserService userService;


}
