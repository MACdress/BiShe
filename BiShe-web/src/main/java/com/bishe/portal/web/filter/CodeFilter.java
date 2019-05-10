package com.bishe.portal.web.filter;

import com.bishe.portal.web.utils.JsonView;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CodeFilter implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        HttpSession session = httpServletRequest.getSession();
        Object user = session.getAttribute("user");
        if (((httpServletRequest.getRequestURI().startsWith(httpServletRequest.getContextPath()+"/userCenter/login")))||((httpServletRequest.getRequestURI().startsWith(httpServletRequest.getContextPath()+"/userCenter/test")))){
            return true;
        }else if (user == null) {
                return true;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
            httpServletResponse.setContentType("charset = UTF-8");
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        httpServletResponse.setContentType("charset = UTF-8");
    }
}
