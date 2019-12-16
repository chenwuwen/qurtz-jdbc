package cn.kanyun.qurtzjdbc.controller;

import cn.kanyun.qurtzjdbc.entity.UserEntity;
import cn.kanyun.qurtzjdbc.service.UserService;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author Kanyun
 */
@Api(tags = "用户模块")
@Slf4j
@Controller
public class LoginController {

    @Resource
    private UserService userService;

    @Resource
    private Producer captchaProducer;

    @GetMapping("/login")
    public String login(HttpServletRequest request) {
        log.debug("用户登录");
        if (request.getSession().getAttribute("author") != null) {
            return "redirect:/index";
        }
        return "login";
    }

    @ApiOperation(value = "/login", notes = "用户登录", httpMethod = "POST", response = String.class)
    @PostMapping("/login")
    public String login(UserEntity user, HttpServletRequest request) {
        log.debug("用户登录");
//        判断验证码
        if (request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY).equals(user.getCaptcha().toLowerCase())) {
            if (userService.login(user) != null) {
                HttpSession session = request.getSession();
                session.setAttribute("author", user.getUserName());
                return "redirect:/index";
            }
        }

        return "login";
    }


    @RequestMapping(value = "verification", method = RequestMethod.GET)
    public ModelAndView verification(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setDateHeader("Expires", 0);
        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        // Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");
        // return a jpeg
        response.setContentType("image/jpeg");
        // create the text for the image
        String capText = captchaProducer.createText().toLowerCase();
        // store the text in the session
        request.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);
        // create the image with the text
        BufferedImage bi = captchaProducer.createImage(capText);
        ServletOutputStream out = response.getOutputStream();
        // write the data out
        ImageIO.write(bi, "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
        return null;
    }

}
