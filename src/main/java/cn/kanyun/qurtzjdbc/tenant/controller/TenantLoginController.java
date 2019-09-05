package cn.kanyun.qurtzjdbc.tenant.controller;

import cn.kanyun.qurtzjdbc.tenant.entity.TenantUser;
import cn.kanyun.qurtzjdbc.tenant.service.TenantUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 租户登录
 *
 * @author Kanyun
 */
@Slf4j
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

    /**
     * 移动端租户登录
     *
     * @param tenantUser
     * @return
     */
    @PostMapping("/mobile/login")
    @ResponseBody
    public Map mobileLogin(@RequestBody TenantUser tenantUser) {
        log.debug("移动端租户登录");
        Map map = new HashMap(3);
        map.put("msg", "");
        map.put("data", null);
        Optional<TenantUser> optionalTenantUser = tenantUserService.login(tenantUser);
        if (optionalTenantUser.isPresent()) {
            map.put("code", 0);
        }else {
            map.put("code", 1);

        }
        return map;
    }

}
