package com.mychaincode.server;


import com.mychaincode.server.controller.SubmitController;
import com.mychaincode.server.service.UserService;
import com.mychaincode.server.utils.EmailUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    @Autowired
    UserService userService;
    @Autowired
    EmailUtil emailUtil;
    @Autowired
    SubmitController submitController;

    //用户删除：√
    @Test
    public void deleteTest() throws JSONException{
        JSONObject json = new JSONObject();
        json = userService.delete("changeTest");
        System.out.println(json.toString());
    }
    @Test
    public void email() {
        String to = "1947113120@qq.com";
        String title = "测试";
        String message = "接收邮件";
        emailUtil.sendMail(to,title,message);
    }

    //用户注册：公司表的department唯一导致用户注册时出错
    @Test
    public void registerTest() throws JSONException {
        JSONObject object = new JSONObject();
        String company = "外来用户";
        String power = "普通用户";
        String isverify = "否";
        String state = "已注册";
        String username = "修改测试";
        String password = "123456789";
        String email = "changeTest";
        String cellphone = "changeTest";
        object = userService.create(username,password,email,company,cellphone,power,isverify,state);
        System.out.println(object.toString());
    }

    //用户列表：√
    @Test
    public void userInfoTest() throws JSONException {
        JSONArray array = new JSONArray();
        array = userService.employees("已注册");
        System.out.println(array.toString());
    }

    //修改用户信息：√
    @Test
    public void changeUserInfoTest() throws JSONException {
        JSONObject object;
        String email = "changeTest";
        String username = "修改测试";
        String cellphone = "changeTest";
        String company = "外来用户";
        String power = "修改信息测试";
        object = userService.changeInfo(email,username,cellphone,company,power);
        System.out.println(object.toString());
    }

    //修改密码测试：√
    @Test
    public void changePasswordTest() throws JSONException {
        JSONObject object;
        String email = "changeTest";
        String oldpassword = "123456789";
        String newpassword = "a123456";
        object = userService.changePassword(email,oldpassword,newpassword);
        System.out.println(object.toString());
    }

}
