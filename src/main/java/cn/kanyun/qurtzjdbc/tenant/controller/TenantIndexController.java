package cn.kanyun.qurtzjdbc.tenant.controller;

import cn.kanyun.qurtzjdbc.tenant.entity.TenantBiz;
import cn.kanyun.qurtzjdbc.tenant.service.TenantBizService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 租户首页
 *
 * @author Kanyun
 */
@Slf4j
@Controller
@RequestMapping("/api/tenant")
public class TenantIndexController {

    @Resource
    private TenantBizService tenantBizService;


    /**
     * 租户首页
     *
     * @return
     */
    @GetMapping("/index")
    public String index(HttpServletRequest request) {
        log.debug("租户成功登陆进入租户首页");
        String domain = (String) request.getSession().getAttribute("domain");
        log.debug("domain:{}", domain);
//        如果是前后端分离开发：登录成功后在这里要把用户的域名保存起来,为什么这么做呢？因为前端页面有不少Ajax请求,而这些请求的URL应该
//        是根据租户的不同而发生变化的,虽然最终都指向了同一个服务,但是需要服务来区分租户的数据源
//        这个前端保存域名的方式可以使用Cookie,或者后台返回json前台使用localStorage保存,然后生成新的Url,至于静态资源,css之类的,由于不走后台服务可以不用管
//        当然这个工作也可以后端来做,由于用户登录时保存了session,所以可以获取session取到域名信息,在AOP中进行拼接
//        但是这种方式在前端会很容易看到服务的地址,这样是否合适?,而且后端做这个很多余？
//        如果前后端不分离，那么使用后端模板引擎,就没有这个问题了,前后端分离,是否会给前端造成一定困扰？

//        String domain = request.getScheme() + "://" + request.getServerName() + ":" + request.getLocalPort();
//        request.getSession().setAttribute("domain", domain);
        return "tenant/index";
    }

    /**
     * 获取租户业务列表
     *
     * @return
     */
    @GetMapping("/getBizList")
    @ResponseBody
    public Map getBizList(HttpServletRequest request) {
        log.debug("租户获取业务列表");
        Map map = new HashMap(3);
        String domain = request.getScheme() + "://" + request.getServerName() + ":" + request.getLocalPort();
        List<TenantBiz> tenantBizs = tenantBizService.queryAll();
        map.put("code", 0);
        map.put("count", tenantBizs.size());
        map.put("msg", "");
        map.put("data", tenantBizs);
        return map;
    }

}
