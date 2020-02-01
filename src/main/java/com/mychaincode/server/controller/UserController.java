package com.mychaincode.server.controller;

import com.mychaincode.server.entity.Users;
import com.mychaincode.server.repository.UsersRepository;
import com.mychaincode.server.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {
    @Autowired
    UserService userService;
    @Autowired
    UsersRepository userInfoRepository;
//    @Autowired
//    CoalPurchaseApplyRepository coalPurchaseApplyRepository;
//    @Autowired
//    AttributeRepository attributeRepository;
//    @Autowired
//    CPACheckRepository cpaCheckRepository;

//    @RequiresGuest
//    @RequestMapping("/authCode")
//    public String authCode(HttpServletRequest request) throws IOException, JSONException {
//        //获取验证码
//        ReadCodeImageUtil readCodeImageUtil = new ReadCodeImageUtil();
//        Map<String,String> map = readCodeImageUtil.getValidateCode();
//        //将验证码存入到Session中进行共享
//        HttpSession session = request.getSession();
//        session.setAttribute("VALIDATECODE",map.get("code"));
//
//        //调用图片文件生成工具
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("authCodePicturePath",map.get("filePath"));
//        return jsonObject.toString();//若写入成功则返回图片路径
//    }

    @RequiresUser
    @RequestMapping("/page")
    public String page() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("result","1");
        return object.toString();
    }

    @RequiresUser
    @RequestMapping("/current")
    public String getAccount() throws JSONException {
        JSONObject json = new JSONObject();
        Users user = (Users) SecurityUtils.getSubject().getPrincipal();
        json.put("email", user.getEmail());
        return json.toString();
    }

    @RequiresUser
    @RequestMapping("/info")
    public String info(@RequestParam(value = "email",required = false) String email) throws JSONException {
        if (email.isEmpty()) {//防止传入空账号
            JSONObject json = new JSONObject();
            json.put("error", "传入账号为空");
            return json.toString();
        }
        return userService.info(email).toString();//返回用户信息
    }

    @RequiresGuest
    @RequestMapping("/login")
    public String login(@RequestParam(value = "account", required = false) String account,
                        @RequestParam(value = "password", required = false) String password
                        ) throws JSONException {
        if (!password.matches("^\\w{2,40}$")) {//长度限制可能需要更改!account.matches("^\\w{2,40}$") ||
            JSONObject json = new JSONObject();
            json.put("error", "账户或密码长度不符合格式要求,请重新检查");
            return json.toString();
        }
        return userService.login(account, password).toString();
    }

    @RequiresUser
    @RequestMapping("/logout")
    public String logout() throws JSONException {
        return userService.logout().toString();
    }

    @RequiresGuest
    @RequestMapping("/register")
    public String register(@RequestParam("username") String username,
                           @RequestParam("email") String email,
                           @RequestParam("password") String password,
                           @RequestParam("cellphone") String cellphone) throws JSONException {
        JSONObject object = new JSONObject();
        String company = "外来用户";
        String power = "普通用户";
        String isverify = "否";
        String state = "已注册";
        object = userService.create(username,password,email,company,cellphone,power,isverify,state);
        return object.toString();
    }

    //用户列表
    @RequiresPermissions("管理人员")
    @RequestMapping("/employees")
    public String employees() throws JSONException {
        JSONArray array = new JSONArray();
        String state = "已注册";
        array = userService.employees(state);
        JSONObject json = new JSONObject();
        json.put("list", array);
        return json.toString();
    }

    /*
    删除用户（只有管理员可以使用）
    传入参数：email: '邮箱'
     */
    @RequiresPermissions("管理人员")
    @RequestMapping("/delete")
    public String delete(@RequestParam("email") String email) throws JSONException {
        JSONObject json = new JSONObject();
        if (email.isEmpty()) {
            json.put("error", "请选择正确信息");
            return json.toString();
        }
        return userService.delete(email).toString();//返回错误信息
    }

    /*
    添加用户（只有管理员可以使用）:公司改为下拉列表
    传入参数：
    用户账户名
    用户密码
    用户姓名
    用户的公司id
    用户联系方式
    用户工作id
    用户家庭住址
    用户email
     */
    @RequiresPermissions("管理人员")
    @RequestMapping("/create")
    public String create(@RequestParam("username") String username,
                         @RequestParam("password") String password,
                         @RequestParam("email") String email,
                         @RequestParam("company") String company,
                         @RequestParam("cellphone") String cellphone,
                         @RequestParam("power") String power,
                         @RequestParam("isverify") String isverify,
                         @RequestParam("state") String state) throws JSONException {
        JSONObject json = new JSONObject();
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(cellphone);
        if (username.isEmpty() || email.isEmpty() || cellphone.isEmpty() || company.isEmpty() || power.isEmpty() || isverify.isEmpty() || state.isEmpty()) {
            System.out.println("输入为空");
            json.put("error", "输入不能为空！");
            return json.toString();
        } else if (!email.matches("^\\w{4,20}$")) {
            json.put("error", "用户账户为4-20个英文字符，请检查输入");
            return json.toString();
        } else if (!password.matches("^\\w{6,20}$")) {
            json.put("error", "用户密码为6-20个英文字符，请检查输入");
            return json.toString();
        } else if (username.length() > 20) {
            json.put("error", "用户名字为10个以下汉字，请检查输入");
            return json.toString();
        } else if (!cellphone.matches("^\\w{8,15}$") || !isNum.matches()) {
            json.put("error", "用户联系方式为8-15个数字，请检查输入");
            return json.toString();
        }
        return userService.create(username,password,email,company,cellphone,power,isverify,state).toString();
    }

    /*
    修改用户信息：只有管理员可以使用
        account: '账户'
        name: '新的姓名',
        company: '新的部门',
        phone: '新的联系方式',
        job: '新的角色'
     */
    @RequiresPermissions("管理人员")
    @RequestMapping("/changeInfo")
    public String changeInfo(@RequestParam("email") String email,
                             @RequestParam("username") String username,
                             @RequestParam("cellphone") String cellphone,
                             @RequestParam("company") String company,
                             @RequestParam("power") String power) throws JSONException {
        JSONObject json = new JSONObject();
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(cellphone);
        if (email.isEmpty() || username.isEmpty() || cellphone.isEmpty()) {
            json.put("error", "输入不能为空！");
            return json.toString();
        } else if (!email.matches("^\\w{3,20}$")) {
            json.put("error", "用户账号为3-20个英文字符，请检查输入");
            return json.toString();
        } else if (username.length() > 20) {
            json.put("error", "用户名不能超过10汉字，请检查输入");
            return json.toString();
        } else if (!cellphone.matches("^\\w{8,15}$") || !isNum.matches()) {
            json.put("error", "用户手机号为8-15个数字，请检查输入");
            return json.toString();
        } else {
            json = userService.changeInfo(email, username, cellphone, company,power);
        }
        return json.toString();//此处调用service中的类来实现修改用户信息
    }

    @RequiresUser
    @RequestMapping("/changePassword")
    public String changePassword(@RequestParam("email") String email,
                                 @RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword) throws JSONException {
        JSONObject json = new JSONObject();
        if (email.isEmpty()) {//防止传入空账号
            json.put("error", "传入账号为空");
            return json.toString();
        }
        if (oldPassword.equals(newPassword)) {
            json.put("error", "新密码与旧密码不能相同");
            return json.toString();
        }
        if (!oldPassword.matches("^\\w{6,20}$") || !newPassword.matches("^\\w{6,20}$")) {//长度限制可能需要更改
            json.put("error", "密码长度为6-20个非中文字符，请检查密码格式");
            return json.toString();
        }
        if (!Pattern.matches("^[A-Za-z0-9-]+$", oldPassword) || !Pattern.matches("^[A-Za-z0-9-]+$", newPassword)) {
            json.put("error", "密码中不能包含除英文字符和数字之外的其他字符");
            return json.toString();
        }
        return userService.changePassword(email, oldPassword, newPassword).toString();//返回处理结果
    }

    @RequiresPermissions("管理人员")
    @RequestMapping("/resetPassword")
    public String resetPassword(@RequestParam("email") String email) throws JSONException {
        JSONObject object = new JSONObject();
        String oldpasswd;
        String newpasswd = "000000";
        Users users = userInfoRepository.getByEmail(email);
        oldpasswd = users.getPasswd();
        object = userService.changePassword(email,oldpasswd,newpasswd);
        return object.toString();
    }

}
