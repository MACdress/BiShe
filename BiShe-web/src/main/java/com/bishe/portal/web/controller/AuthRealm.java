package com.bishe.portal.web.controller;

import com.bishe.portal.dao.TbUsersDao;
import com.bishe.portal.model.po.TbUsersPo;
import com.bishe.portal.service.UserService;
import com.bishe.portal.service.utils.SessionContext;
import com.bishe.portal.web.interfaces.SessionUser;
import com.bishe.portal.web.vo.AuthUser;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;

public class AuthRealm extends AuthorizingRealm {
        @Resource
        private TbUsersDao tbUsersDao;

        @Resource
        private UserService userService;

        private String pass;

        /**
         * 登录:
         *
         */
        @Override
        protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
            if (principals == null)
                throw new AuthorizationException("PrincipalCollection method argument cannot be null.");
            // 获取当前登录用户
            SessionUser user = SessionContext.getAuthUser();
            if (user == null) {
                return null;
            }
            // 设置权限
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            // 获取用户权限并设置 以供shiro框架
            info.setStringPermissions(user.getPermission());
            return info;
        }

        /*
         * 用户验证
         *
         */
        @Override
        protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
            UsernamePasswordToken token = (UsernamePasswordToken) token;
            String username = token.getUsername();
            String password = String.valueOf(token.getPassword());
            AuthUser authUser = null;
            try {
                TbUsersPo tbUsersPo = userService.getByUserTel(username);
                if (null != tbUsersPo) {
                    authUser = new AuthUser();
                    authUser.setRealname(tbUsersPo.getName());
                    authUser.setTel(tbUsersPo.getTel());
                } else {
                    throw new AuthenticationException("## user password is not correct! ");
                }
            }catch (Exception e) {
                throw new AuthenticationException("## user password is not correct! ");
            }

            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(authUser, password, getName());
            return (AuthorizationInfo) info;
        }

        //init-method 配置.
        public void setCredentialMatcher(){
            HashedCredentialsMatcher  credentialsMatcher = new HashedCredentialsMatcher();
            credentialsMatcher.setHashAlgorithmName("MD5");//MD5算法加密
            credentialsMatcher.setHashIterations(1024);//1024次循环加密
            setCredentialsMatcher(credentialsMatcher);
        }

    }
}
