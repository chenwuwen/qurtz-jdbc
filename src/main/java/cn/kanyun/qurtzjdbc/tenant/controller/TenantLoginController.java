package cn.kanyun.qurtzjdbc.tenant.controller;

import cn.kanyun.qurtzjdbc.tenant.entity.TenantUser;
import cn.kanyun.qurtzjdbc.tenant.service.TenantUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * 租户登录
 *
 * @author Kanyun
 */
@Controller
@RequestMapping("/api/tenant")
public class TenantLoginController {

    @Resource
    private TenantUserService tenantUserService;

    /**
     * 返回租户登录页
     *
     * @return
     */
    @GetMapping(value = {"", "login", "/"})
    public String login() {
        return "tenant/login";
    }

    /**
     * 租户登录
     *
     * @param tenantUser
     * @return
     */
    @PostMapping("/login")
    public String login(TenantUser tenantUser) {
        Optional<TenantUser> optionalTenantUser = tenantUserService.login(tenantUser);
        return optionalTenantUser.isPresent() ? "redirect:/api/tenant/index" : "tenant/login";
    }


}
