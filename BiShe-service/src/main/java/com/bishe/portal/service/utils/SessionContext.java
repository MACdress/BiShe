//package com.bishe.portal.service.utils;
//
//import com.bishe.portal.web.interfaces.SessionUser;
//import org.apache.shiro.SecurityUtils;
//import org.apache.shiro.authc.AuthenticationException;
//import org.apache.shiro.subject.Subject;
//
//import javax.servlet.http.HttpServletRequest;
//
///**
// *  @Author:GaoPan
// *  * @Date:2018/12/20 21:44
// *  * @Version 1.0
// */
//public class SessionContext {
//    /**
//     * 其他人不得占用
//     */
//    public static final String IDENTIFY_CODE_KEY = "_const_identify_code_key_";
//    /**
//     * // 其他人不得占用
//     */
//    public static final String AUTH_USER_KEY = "_const_auth_user_key_";
//
//    public static Long getUserTel(){
//        if(null != getAuthUser()){
//            return getAuthUser().getTel();
//        }
//        return null;
//    }
//
//    public static String getUsername(){
//        if(null != getAuthUser()){
//            return getAuthUser().getUsername();
//        }
//        return null;
//    }
//
//    /**
//     * 获取權限登录用户
//     * @return
//     */
//    public static SessionUser getAuthUser() {
//        if(null != SecurityUtils.getSubject().getPrincipal()){
//            return (SessionUser) SecurityUtils.getSubject().getPrincipal();
//        }
//        return null;
//    }
//
//    /**
//     * 获取验证码
//     * @param request
//     * @return
//     */
//    public static String getIdentifyCode(HttpServletRequest request) {
//        if (request.getSession().getAttribute(IDENTIFY_CODE_KEY) != null) {
//            return getAttribute(request, IDENTIFY_CODE_KEY).toString();
//        } else {
//            return null;
//        }
//    }
//
//    /**
//     * 获取属性
//     * @param request
//     * @param key
//     * @return
//     */
//    public static Object getAttribute(HttpServletRequest request, String key) {
//        return request.getSession().getAttribute(key);
//    }
//
//    /**
//     * 设置属性
//     * @param request
//     * @param key
//     * @param value
//     */
//    public static void setAttribute(HttpServletRequest request, String key, Object value) {
//        request.getSession().setAttribute(key, value);
//    }
//
//    /**
//     * 删除属性
//     * @param request
//     * @param key
//     */
//    public static void removeAttribute(HttpServletRequest request, String key) {
//        request.getSession().removeAttribute(key);
//    }
//
//    public static boolean isLogin(){
//        Subject currentUser = SecurityUtils.getSubject();
//        if(null != currentUser && null != currentUser.getPrincipal()){
//            return true;
//        }
//        return false;
//    }
//
//    /**
//     * Shiro Logout
//     */
//    public static void shiroLogout(){
//        Subject currentUser = SecurityUtils.getSubject();
//        try {
//            currentUser.logout();
//        } catch (AuthenticationException e) {
//            e.printStackTrace();
//        }
//    }
//}
