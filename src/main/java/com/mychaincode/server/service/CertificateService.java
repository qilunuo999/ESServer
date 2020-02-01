package com.mychaincode.server.service;

import com.mychaincode.server.entity.Certificate;
import com.mychaincode.server.entity.Certificateaudit;
import com.mychaincode.server.entity.Users;
import com.mychaincode.server.repository.CertificateRepository;
import com.mychaincode.server.repository.CertificateauditRepository;

import com.mychaincode.server.repository.CompanyRepository;
import com.mychaincode.server.repository.UsersRepository;
import com.mychaincode.server.utils.EmailUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.sql.Timestamp;
import java.util.List;

@Service
public class CertificateService {

    @Autowired
    CertificateRepository certificateRepository;
    @Autowired
    CertificateauditRepository certificateauditRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    EmailUtil emailUtil;
    @Autowired
    UsersRepository usersRepository;

    /*
    * 保存用户上传的证书认证申请
    * @param certificate 证书文件名
    * @param user 登陆用户
    * @param filetype 证书类型
     */
    public JSONObject saveApplying(String certificate, String id, String email, String filetype) throws JSONException {
        JSONObject json = new JSONObject();
        Certificateaudit certificateaudit = new Certificateaudit();
        certificateaudit.setCertificate(certificate);
        certificateaudit.setCid(companyRepository.getIdByEmail(email));
        certificateaudit.setUid(Integer.valueOf(id));
        certificateaudit.setFiletype(filetype);
        certificateaudit.setState("未认证");
        certificateauditRepository.insert(certificateaudit);
        return json;
    }

    /*
    **c查看证书申请列表
     */
    public JSONObject getApplyingList() throws JSONException {
        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        List<Certificateaudit> applyList = certificateauditRepository.getByPageNo();

        for(Certificateaudit i:applyList){
            JSONObject temp = new JSONObject();//用于临时保存查到的数据
            temp.put("id",i.getId());
            temp.put("username",usersRepository.getNameById(i.getUid()));
            temp.put("company",companyRepository.getCompanyById(i.getCid()));
            temp.put("filetype",i.getFiletype());
            temp.put("certificate",i.getCertificate());
            array.put(temp);
        }
        jsonObject.put("list",array);
        return jsonObject;
    }

    /*
    **下载证书
    * 证书id
     */
    public byte[] getCertificateInfo(int id)  {
        try {
            Certificateaudit certificateaudit = certificateauditRepository.getById(id);
            BufferedReader in = new BufferedReader(new FileReader(certificateaudit.getCertificate()));
            StringBuilder file = new StringBuilder();
            while (in.ready()) {
                file.append(in.readLine());
            }
            byte[] filebyte = file.toString().getBytes();
            in.close();
            return filebyte;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
    **证书申请处理
    * @param id 证书id
    * @param state 管理员认证结果
     */
    public JSONObject solveApplying(int id, String state) throws JSONException, CertificateException, FileNotFoundException {
        JSONObject json = new JSONObject();

        if(state.equalsIgnoreCase("认证")){
            certificateauditRepository.update("已认证",id);
            Certificateaudit certificateaudit = certificateauditRepository.getById(id);
            Certificate certificate = new Certificate();
            CertificateFactory CF = CertificateFactory.getInstance("X.509"); // 从证书工厂中获取X.509的单例类
            FileInputStream fileIn = new FileInputStream("D:\\staticserver\\verifyfile\\" + certificateaudit.getCertificate()); // 将证书读入文件流
            java.security.cert.Certificate C = CF.generateCertificate(fileIn);  // 将文件流的证书转化为证书类
            X509Certificate cer = (X509Certificate)C;
            certificate.setId(cer.getSerialNumber().toString());
            certificate.setCertificate(certificateaudit.getCertificate());
            certificate.setFiletype(certificateaudit.getFiletype());
            certificate.setStarttime(cer.getNotBefore());
            certificate.setEndtime(cer.getNotAfter());
            certificateRepository.insert(certificate);
            emailUtil.sendMail(usersRepository.getEmailById(certificateaudit.getUid()),"证书验证成功","您提交验证的证书已通过验证，可随时用于制作印章。");
        }else if(state.equalsIgnoreCase("驳回")){
            certificateauditRepository.update("已驳回",id);
            Certificateaudit certificateaudit = certificateauditRepository.getById(id);
            emailUtil.sendMail(usersRepository.getEmailById(certificateaudit.getUid()),"证书验证失败","您提交验证的证书已被驳回，请确认您提交的证书是否是由权威机构认证");
        }
        return json;
    }


    /*
    **证书校验，用于校验客户端制作印章时使用的证书是否已认证
    * @param serialNumber 证书序列号，存于数据库中
     */
    public JSONObject checkCertificate(String serialNumber) throws JSONException{
        JSONObject json = new JSONObject();
        Certificate certificate = certificateRepository.getById(serialNumber);
        if(certificate == null){
            json.put("error","您使用的证书未通过认证，如果您继续进行制作印章操作，制作的印章将被认定为不安全印章");
            return json;
        }else{
            json.put("message","校验通过");
            return json;
        }
    }
}
