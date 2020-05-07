package com.jtt.hhl.testapi.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.RequestWrapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * java类作用描述
 *
 * @author Herman
 * @createDate 2020/4/4 20:01
 */
public class ApiInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        MyHttpServletRequestWrapper myHttpServletRequestWrapper = new MyHttpServletRequestWrapper(request);

        String requestUrl = request.getRequestURI();

        Map<String,String[]> map = request.getParameterMap();
        for (Map.Entry<String, String[]> entry: map.entrySet()) {
            System.out.println("===map=== " + entry.getKey()+","+ ((String[])entry.getValue())[0]);
        }

        String name = request.getParameter("name");
        System.out.println("===name=== " + name);


        System.out.println("====input=== " + myHttpServletRequestWrapper.getBodyString(request));

        return true;
    }
}
