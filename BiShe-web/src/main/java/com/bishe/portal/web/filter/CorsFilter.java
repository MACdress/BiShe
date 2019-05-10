package com.bishe.portal.web.filter;


import com.alibaba.druid.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CorsFilter implements Filter {

    private boolean isCross = false;

    @Override
    public void init(FilterConfig filterConfig) {
        String isCrossStr = filterConfig.getInitParameter("IsCross");
        isCross = isCrossStr.equals("true") ? true : false;
        System.out.println(isCrossStr);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        if (isCross) {
            String origin = httpServletRequest.getHeader("origin");// 获取源站
            String requestHeaders = httpServletRequest.getHeader("Access-Control-Request-Headers");
            if (StringUtils.isEmpty(requestHeaders)){
                requestHeaders = "";
            }
            System.out.println("拦截请求: " + httpServletRequest.getServletPath());
            //允许所有地址
            httpServletResponse.setHeader("Access-Control-Allow-Origin", origin);
            //允许的方法
            httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST, GET,PUT, OPTIONS, DELETE,OPTIONS");
            //所能看到的请求头
            httpServletResponse.setHeader("Access-Control-Allow-Headers","Accept, Origin, XRequestedWith, Content-Type, LastModified," + requestHeaders);
            //浏览器所能获取到的请求头
            httpServletResponse.setHeader("Access-Control-Expose-Headers","Authorization,Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With");
            httpServletResponse.setHeader("Cache-Control","no-cache");
            httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
        }
//        HttpSession session = httpServletRequest.getSession();
//        Object user = session.getAttribute("user");
//        if ((httpServletRequest.getRequestURI().startsWith(httpServletRequest.getContextPath()+"/userCenter/login"))){
//            filterChain.doFilter(httpServletRequest, httpServletResponse);
//        }else if (user == null) {
//            String render = JsonView.render(404, "未登录，请先登录");
//            httpServletResponse.setCharacterEncoding("UTF-8");
//            httpServletResponse.getWriter().write(render);
//            return;
//        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    @Override
    public void destroy() {
        isCross = false;
    }

}
