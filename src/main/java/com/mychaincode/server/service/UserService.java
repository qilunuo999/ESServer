package com.mychaincode.server.service;

import com.mychaincode.server.entity.Departmenttable;
import com.mychaincode.server.entity.Users;
import com.mychaincode.server.repository.DepartmenttableRopository;
import com.mychaincode.server.repository.UsersRepository;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UsersRepository userinfoRepository;
    @Autowired
    private DepartmenttableRopository departmenttableRopository;
//    @Autowired
//    private AttributeRepository attributeRepository;

    //用户登陆功能
    public JSONObject login(String account, String password) throws JSONException {
        JSONObject json = new JSONObject();
        String power = "管理人员";
        UsernamePasswordToken token = new UsernamePasswordToken(account, password);
        Subject subject = SecurityUtils.getSubject();
        Users users;
        try {
//            System.out.println("UsernamePasswordToken:");
//            System.out.println("hashCode:" + token.hashCode());
//            System.out.println("Principal:" + token.getPrincipal());
//            System.out.println("Credentials:" + String.valueOf((char[]) token.getCredentials()));
//            System.out.println("host:" + token.getHost());
//            System.out.println("Username:" + token.getUsername());
//            System.out.println("Password:" + String.valueOf(token.getPassword()));
            subject.login(token);
            users = userinfoRepository.getByEmail(account);
            if (users.getPower().equals(power)) {
                json.put("result", "管理人员");
                json.put("username",users.getUsername());
                json.put("department",users.getDepartment());
                return json;
            } else {
                json.put("result", "普通用户");
                json.put("username",users.getUsername());
                json.put("department",users.getDepartment());
                return json;
            }

        } catch (UnknownAccountException e) {
            json.put("error", "用户未注册");
        } catch (IncorrectCredentialsException e) {
            json.put("error", "密码错误");
        }
        return json;
    }

    //用户登出
    public JSONObject logout() throws JSONException{
        Subject subject = SecurityUtils.getSubject();
        subject.logout();

        JSONObject json = new JSONObject();
        json.put("result", "成功退出");
        return json;
    }

    /* 用户列表显示√
    *
     */
    public JSONArray employees(String state) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        List<Users> user = userinfoRepository.employees(state);
        for (Users userInfo:user){
            JSONObject json = new JSONObject();
            json.put("id",userInfo.getId());
            json.put("username",userInfo.getUsername());
            json.put("email",userInfo.getEmail());
            json.put("department",userInfo.getDepartment());
            json.put("cellphone",userInfo.getCellphone());
            json.put("power",userInfo.getPower());
            json.put("isverify",userInfo.getIsverify());
            jsonArray.put(json);
        }
        return jsonArray;
    }

    /*
        删除用户√
     */
    public JSONObject delete(String email) throws JSONException {
        JSONObject json = new JSONObject();
        Users user = userinfoRepository.getByEmail(email);
        if (user == null){
            json.put("error","不存在该用户");
        } else{
            userinfoRepository.delete(email,"已注销");
            json.put("result","删除成功");
        }
        return json;
    }

    /*
    添加用户√create(account,password,name,compan.getId(),phone,attribute.getId()).toString();
     */
    public JSONObject create(String username, String passwd, String email, String company, String cellphone,String power,String isverify,String state) throws JSONException {
        JSONObject json = new JSONObject();
        Users user = userinfoRepository.getByEmail(email);
        if (user != null){
            json.put("error","已存在该用户");
        } else{
            user = new Users();
            user.setUsername(username);
            user.setPasswd(passwd);
            user.setEmail(email);
            user.setDepartment(company);
            user.setCellphone(cellphone);
            user.setPower(power);
            user.setIsverify(isverify);
            user.setState(state);
            userinfoRepository.insert(user);
            json.put("result","添加成功");
        }
        return json;
    }


    /*修改用户信息
    *
     */
    public JSONObject changeInfo(String email, String username, String cellphone, String company,String power) throws JSONException {
        JSONObject json = new JSONObject();
        Users userInfo = userinfoRepository.getByEmail(email);
        if (userInfo == null){
            json.put("error","该用户不存在");
            return json;
        }
        Users user = new Users();
        userInfo.setEmail(email);
        userInfo.setUsername(username);
        userInfo.setCellphone(cellphone);
        userInfo.setDepartment(company);
        userInfo.setPower(power);
        userinfoRepository.updateAccountInfo(userInfo);
        json.put("result","修改成功");
        return json;
    }

    /*
    修改密码
     */
    public JSONObject changePassword(String email, String oldPassword, String newPassword) throws JSONException {
        JSONObject json = new JSONObject();
        Users user = userinfoRepository.getByEmail(email);
        if (user == null) {
            json.put("error", "用户不存在");
        } else if (oldPassword.equals(user.getPasswd())) {//校验旧密码是否正确
            user.setPasswd(newPassword);
            user.setEmail(email);
            userinfoRepository.updatePassword(user);
            json.put("result", "修改密码成功");
        } else {
            json.put("error", "原密码错误");
        }
        return json;
    }

    //获取当前用户信息
    public JSONObject info(String account)throws JSONException{
        JSONObject json = new JSONObject();
        Users user = userinfoRepository.getByEmail(account);
        if(user == null){
            json.put("error", "您查找的用户不存在");
        }else{
            json.put("id",user.getId());
            json.put("username",user.getUsername());
            json.put("email",user.getEmail());
            json.put("cellphone",user.getCellphone());
        }
        return json;
    }

}
