package com.mychaincode.server.config;

import com.mychaincode.server.entity.Users;
import com.mychaincode.server.repository.UsersRepository;
import com.mychaincode.server.utils.PBKDF2;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Component("authorizer")
public class ShiroRealm extends AuthorizingRealm {
    @Autowired
    private UsersRepository usersRepository;

    //获取角色/权限信息用于授权验证
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {//PrincipalCollection用于聚合多个身份信息
        Users userInfo = (Users) getAvailablePrincipal(principalCollection);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addStringPermission(userInfo.getPower());
        return info;
    }

    //记住账号密码的登陆
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String account = token.getUsername();
        String password = new String(token.getPassword());

        Users user = usersRepository.getByEmail(account);//usersRepository.getByUsername(account);
        if (user == null) {
            throw new UnknownAccountException();
        } else {
            try {
                if (PBKDF2.verify(password,PBKDF2.getSalt(),user.getPasswd())) {
                    throw new IncorrectCredentialsException();
                }
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                e.printStackTrace();
            }
        }
        return new SimpleAuthenticationInfo(user, password, getName());
    }
}
