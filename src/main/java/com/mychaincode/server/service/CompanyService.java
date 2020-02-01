package com.mychaincode.server.service;

import com.mychaincode.server.entity.Company;
import com.mychaincode.server.repository.CompanyRepository;
import com.mychaincode.server.repository.UsersRepository;
import com.mychaincode.server.utils.EmailUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {

    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    EmailUtil emailUtil;
    @Autowired
    UsersRepository usersRepository;
    /*
    **企业信息提交
    * @param email 用户邮箱
    * @param companyname 公司名称
    * @param creditcode 社会统一信用代码
    * @param verifyinfo 用户提交的企业申请信息
    * @param license 营业执照图片名称
     */
    public JSONObject submitCompanyInfo(String email, String companyname, String creditcode,
                                        String verifyInfo, String license) throws JSONException {
        JSONObject json = new JSONObject();
        Company company = new Company();
        company.setCompany(companyname);
        company.setCreditcode(creditcode);
        company.setEmail(email);
        company.setLicense(license);
        company.setVerifyinfo(verifyInfo);
        company.setState("未认证");
        companyRepository.insert(company);
        return json;
    }

    /*
    **待审核企业信息列表
     */
    public JSONObject getUnderApprovalInfo() throws JSONException{
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        List<Company> companyList = companyRepository.getUnderApprovalInfoList();
        for(Company i:companyList){
            JSONObject json = new JSONObject();
            String id = usersRepository.getIdByEmail(i.getEmail());
            json.put("id",id);
            json.put("username",usersRepository.getNameById(Integer.valueOf(id)));
            json.put("email",usersRepository.getEmailById(Integer.valueOf(id)));
            json.put("company",i.getCompany());
            jsonArray.put(json);
        }
        jsonObject.put("list",jsonArray);
        return jsonObject;
    }

    /*
    **获取企业信息详情
    * @param id 企业id
     */
    public JSONObject getCompanyInfo(String  company) throws JSONException{
        JSONObject jsonObject = new JSONObject();

        Company companyInfo = companyRepository.getInfoByEmail(company);
        jsonObject.put("username",usersRepository.getNameByEmail(companyInfo.getEmail()));
        jsonObject.put("email",companyInfo.getEmail());
        jsonObject.put("company",companyInfo.getCompany());
        jsonObject.put("creditcode",companyInfo.getCreditcode());
        jsonObject.put("verifyinfo",companyInfo.getVerifyinfo());
        jsonObject.put("license",companyInfo.getLicense());

        return jsonObject;
    }

    /*
    **处理企业认证信息
    * @param id 企业id
    * @param state 管理员认证结果
     */
    public JSONObject solveAppling(String email, String state) throws JSONException{
        JSONObject jsonObject = new JSONObject();
        System.out.println(email);
        if(state.equalsIgnoreCase("认证")){
            companyRepository.update("已认证",email);
            Company company = companyRepository.getInfoByEmail(email);
            emailUtil.sendMail(company.getEmail(),"企业认证成功","您提交的企业信息已成功认证。");
        }else if(state.equalsIgnoreCase("驳回")){
            companyRepository.update("已驳回",email);
            Company company = companyRepository.getInfoByEmail(email);
//            System.out.println(company.getEmail());
            emailUtil.sendMail(company.getEmail(),"企业认证失败",
                    "您提交的企业信息没能认证成功，请检查提交的信息是否正确，如有疑问请联系客服人员");
        }
        jsonObject.put("message","认证完成");
        return jsonObject;
    }
}
