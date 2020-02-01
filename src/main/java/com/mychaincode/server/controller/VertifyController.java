package com.mychaincode.server.controller;

import com.mychaincode.server.service.CertificateService;
import com.mychaincode.server.service.CompanyService;
import com.mychaincode.server.utils.EmailUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.security.cert.CertificateException;

@RestController
@RequestMapping("/vertify")
public class VertifyController  extends BaseController {

    @Autowired
    CompanyService companyService;
    @Autowired
    CertificateService certificateService;
    @Autowired
    EmailUtil emailUtil;

    /*
     * 获取未认证企业信息列表
     */
    @RequestMapping("/companytable")
    public String getCompanyTable() throws JSONException {
        return companyService.getUnderApprovalInfo().toString();
    }

    /*
     * 获取未认证企业详细信息
     * @param id 企业id
     * @param company 企业名
     */
    @RequestMapping("/companyinfo")
    public String getCompanyInfo(@RequestParam(value = "company" , required = false) String company) throws JSONException{
        if(company.isEmpty()){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("error","传入的企业ID或企业名为空");
            return jsonObject.toString();
        }
        return companyService.getCompanyInfo(company).toString();
    }

    /*
     * 获取未审核证书信息列表
     */
    @RequestMapping("/verifytable")
    public String getVerifyTable()throws JSONException{
        return certificateService.getApplyingList().toString();
    }

    /*
    公司信息驳回
     */
    @RequestMapping("/companyReject")
    public String companyReject (@RequestParam("email") String email,@RequestParam("state") String state) throws JSONException {
        JSONObject json = new JSONObject();
        json = companyService.solveAppling(email,state);
        return json.toString();
    }

    /*
    公司信息通过
     */
    @RequestMapping("/confirm")
    public String certify (@RequestParam("email") String email,@RequestParam("state") String state) throws JSONException, CertificateException, FileNotFoundException {
        JSONObject json = new JSONObject();
        json = companyService.solveAppling(email,state);
        return json.toString();
    }

    /*
    * 客户端证书验证
    * @param serialNumber 证书序列号
    * @param certificate
    */
    @RequestMapping("/verifyCertificate")
    public String verifyCertificate(@RequestParam("serialNumber")String serialNumber,@RequestParam("certificate")String certificate){

    }

    /*
    * 已盖章文件验证
    */
    @RequestMapping("/verifySealedFile")
    public String verifySealedFile(){

    }

}
