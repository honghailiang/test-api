package com.jtt.hhl.testapi.interceptor;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * java类作用描述
 *
 * @author Herman
 * @createDate 2020/5/7 20:34
 */
@WebFilter(filterName="httpServletRequestReplacedFilter",urlPatterns={"/*"})
public class HttpServletRequestReplacedFilter implements Filter {
    @Override
    public void destroy() {
        System.out.println("--------------过滤器销毁------------");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        ServletRequest requestWrapper = null;
        if(request instanceof HttpServletRequest) {
            requestWrapper = new MyHttpServletRequestWrapper((HttpServletRequest) request);
        }
        if(requestWrapper == null) {
            chain.doFilter(request, response);
        } else {
            chain.doFilter(requestWrapper, response);
        }
    }
    @Override
    public void init(FilterConfig arg0) throws ServletException {
        System.out.println("--------------过滤器初始化------------");
    }

}
