package cn.kanyun.qurtzjdbc.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
/**
 * @author Kanyun
 */
@Slf4j
@Component
@WebFilter(urlPatterns = "/tenant/**", filterName = "baseFilter")
public class TenantFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String serverName = servletRequest.getServerName();
        log.debug("请求的域名,不包括协议：{}", serverName);
        ThreadLocal<String> threadLocal = new ThreadLocal<>();
        threadLocal.set(serverName);
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
