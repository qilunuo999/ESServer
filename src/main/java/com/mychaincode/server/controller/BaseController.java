package com.mychaincode.server.controller;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class BaseController {
    /**
     * 登录认证异常
     *
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler({ UnauthenticatedException.class, AuthenticationException.class })
    public String authenticationException(HttpServletRequest request, HttpServletResponse response) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("error","您已经登陆了，请不要按返回按钮重新登陆。如果要重新登陆请关闭浏览器重新进入登陆网页。");
        return json.toString();
    }

    /**
     * 权限异常
     *
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler({ UnauthorizedException.class, AuthorizationException.class })
    public String authorizationException(HttpServletRequest request, HttpServletResponse response) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("error","您当前所使用的用户没有该权限。");
        return json.toString();
    }
}