//package com.bishe.portal.service.shiro;
//
//import com.bishe.portal.model.po.TbUsersPo;
//import com.bishe.portal.service.UserService;
//import com.bishe.portal.service.utils.Encryption;
//import org.apache.shiro.SecurityUtils;
//import org.apache.shiro.authc.*;
//import org.apache.shiro.authz.AuthorizationInfo;
//import org.apache.shiro.authz.SimpleAuthorizationInfo;
//import org.apache.shiro.realm.AuthorizingRealm;
//import org.apache.shiro.session.Session;
//import org.apache.shiro.subject.PrincipalCollection;
//
//import javax.annotation.Resource;
//
///**
// * @author gaopan31
// */
//public class MyReam extends AuthorizingRealm {
//    @Resource
//    private UserService userService;
//
//    @Override
//    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
//        String account = (String) principalCollection.getPrimaryPrincipal();
//        TbUsersPo tbUser = userService.getByUserAccount(account);
//        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
//        if(tbUser.getPermission()==1){
//            authorizationInfo.addStringPermission("/admin");
//        }
//        return authorizationInfo;
//    }
//
//    @Override
//    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
//        String account = (String)authenticationToken.getPrincipal();
//        String password = new String((char[]) authenticationToken.getCredentials());
//        TbUsersPo tbUser = userService.getByUserAccount(account);
//        if (tbUser != null) {
//            String newPwd = Encryption.getPwd(tbUser.getSale(), password);
//            if (newPwd.equals(tbUser.getPwd())) {
//                System.out.println("登录成功");
//                Session session = SecurityUtils.getSubject().getSession();
//                session.setAttribute("userInfo", tbUser);
//                session.setAttribute("userInfoId", tbUser.getId());
//                return new SimpleAuthenticationInfo(account, password, getName());
//            }else{
//                System.out.println("密码错误");
//                throw new IncorrectCredentialsException();
//            }
//        }else{
//            System.out.println("账号不存在");
//            throw new UnknownAccountException();
//        }
//    }
//}
