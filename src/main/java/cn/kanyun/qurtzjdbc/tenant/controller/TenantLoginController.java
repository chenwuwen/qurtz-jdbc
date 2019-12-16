package cn.kanyun.qurtzjdbc.tenant.controller;

import cn.kanyun.qurtzjdbc.tenant.entity.TenantUser;
import cn.kanyun.qurtzjdbc.tenant.service.TenantUserService;
import cn.kanyun.qurtzjdbc.util.JwtUtil;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 租户登录
 *
 * @author Kanyun
 */
@Api(value = "/api/tenant", tags = "租户接口")
@Slf4j
@Controller
@RequestMapping("/api/tenant")
public class TenantLoginController {

    @Resource
    private TenantUserService tenantUserService;

    @Resource
    private RedisCacheManager redisCacheManager;

    /**
     * 返回租户登录页
     *
     * @return
     */
    @ApiOperation(value = "/", notes = "租户登录页面",httpMethod = "GET")
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
    @ApiOperation(value = "/login", notes = "租户登录接口", httpMethod = "POST", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenantUser", value = "租户用户实体", dataType = "TenantUser", paramType = "body", required = true)
    })
    @PostMapping("/login")
    public String login(TenantUser tenantUser, HttpServletRequest request) {
        Optional<TenantUser> optionalTenantUser = tenantUserService.login(tenantUser);
        if (optionalTenantUser.isPresent()) {

//            将域名保存到公共缓存中,如果是服务间调用,也可以从缓存中获取域名,从而得到数据源的key
            String bearer = JwtUtil.buildJWT("tenant");
            return "redirect:/api/tenant/index";
        }
        return "tenant/login";
    }

    /**
     * 移动端租户登录
     *
     * @param tenantUser
     * @return
     */
    @ApiOperation(value = "/mobile/login", notes = "移动端租户登录接口", httpMethod = "POST", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenantUser", value = "租户用户实体", dataType = "TenantUser", paramType = "body", required = true)
    })
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
        } else {
            map.put("code", 1);

        }
        return map;
    }

}
