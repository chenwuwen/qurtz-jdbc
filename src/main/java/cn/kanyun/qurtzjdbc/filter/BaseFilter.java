package cn.kanyun.qurtzjdbc.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * @author KANYUN
 */
@Slf4j
@Component
@WebFilter(urlPatterns = "/**", filterName = "baseFilter")
public class BaseFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String domain = request.getScheme() + "://" + request.getServerName();
        log.debug("请求的域名：{}", domain);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}