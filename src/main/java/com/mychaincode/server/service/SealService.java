package com.mychaincode.server.service;

import com.mychaincode.server.entity.Sealedfile;
import com.mychaincode.server.entity.Sealtable;
import com.mychaincode.server.entity.Users;
import com.mychaincode.server.repository.CertificateRepository;
import com.mychaincode.server.repository.SealedfileRepository;
import com.mychaincode.server.repository.SealtableRepository;
import com.mychaincode.server.repository.UsersRepository;
import org.apache.catalina.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SealService {

    @Autowired
    CertificateRepository certificateRepository;
    @Autowired
    SealtableRepository sealtableRepository;
    @Autowired
    SealedfileRepository sealedfileRepository;
    @Autowired
    UsersRepository usersRepository;

    /*
    * 新制印章信息提交
    * @param sealId 印章Id
    * @param sealName 印章名
    * @param certificateName 证书名
    * @param sealType 印章类型
    * @param info 印章认证描述
     */
    public JSONObject submitSealInfo(Users user, String sealId, String sealName, String certificateName, String sealType, String info)throws JSONException {
        JSONObject json = new JSONObject();

        Sealtable sealtable = sealtableRepository.getSealByName(sealName);
        if (sealtable != null){
            json.put("error","您所使用的印章名已存在，请改为其他名字。");
            return json;
        }

        sealtable = new Sealtable();
        sealtable.setSealid(sealId);
        sealtable.setSealname(sealName);
        sealtable.setUid(user.getId());
        sealtable.setCertificateid(String.valueOf(certificateRepository.getIdByName(certificateName)));
        sealtable.setSealtype(sealType);
        sealtable.setInfo(info);
        sealtableRepository.insert(sealtable);
        json.put("message","印章创建成功。");

        return json;
    }

    /*
    *
     */

    /*
    *
     */
    public int findLabel(int id,String labelMessage){
        JSONArray array = new JSONArray();
        JSONObject object = new JSONObject();
        int flag = 1;
        List<Sealedfile> sealedfile;
        sealedfile = sealedfileRepository.findLabel(id);
        for (Sealedfile file:sealedfile) {
            if (labelMessage != file.getLabel()) {
                flag = 0;
                return 0;
            }
        }
        return 1;
    }

}
