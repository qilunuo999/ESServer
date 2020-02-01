package com.mychaincode.server.controller;

import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController extends BaseController {

    @RequiresGuest
    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequiresUser
    @RequestMapping("/index")
    public String index(){
        return "index";
    }

    @RequiresUser
    @RequestMapping("/administrators")
    public String administrators(){
        return "administrators";
    }

    @RequiresGuest
    @RequestMapping("/register")
    public String register(){
        return "register";
    }
}
