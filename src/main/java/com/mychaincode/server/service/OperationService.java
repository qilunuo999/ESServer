package com.mychaincode.server.service;

import com.mychaincode.server.entity.Department;
import com.mychaincode.server.entity.Peers;
import com.mychaincode.server.entity.ServiceSetting;
import com.mychaincode.server.entity.Users;
import com.mychaincode.server.repository.DepartmentRepository;
import com.mychaincode.server.repository.OrgsettingRepository;
import com.mychaincode.server.repository.PeersRepository;
import com.mychaincode.server.repository.ServiceSettingRepository;
import com.tencentcloudapi.tbaas.v20180416.models.QueryRequest;
import com.tencentcloudapi.tbaas.v20180416.models.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
// 导入对应产品模块的client
import com.tencentcloudapi.tbaas.v20180416.TbaasClient;
// 导入要请求接口对应的request response类
import com.tencentcloudapi.tbaas.v20180416.models.InvokeRequest;
import com.tencentcloudapi.tbaas.v20180416.models.InvokeResponse;

import java.util.List;

@Service
public class OperationService {
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    OrgsettingRepository orgsettingRepository;
    @Autowired
    PeersRepository peersRepository;
    @Autowired
    ServiceSettingRepository serviceSettingRepository;

    //将数据插入区块链
    public String operateChaincode(Users users,String funcname, String args, String state){
        Department department = new Department();
        department.setDepartment(users.getUdepartment());
        String orgname = departmentRepository.getOrgname(department);
        ServiceSetting serviceSetting = serviceSettingRepository.getServiceSetting();
        List<Peers> peers = peersRepository.getPeersInfo();

        try{
            // 实例化一个认证对象，入参需要传入腾讯云账户密钥对secretId，secretKey
            Credential cred = new Credential(serviceSetting.getScriptid(), serviceSetting.getScriptkey());
            // 设置访问域名
            // SDK会自动指定域名。通常是不需要特地指定域名的，但是如果您访问的是金融区的服务，
            // 则必须手动指定域名，例如云服务器的上海金融区域名： tbaas.ap-shanghai-fsi.tencentcloudapi.com
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("tbaas.tencentcloudapi.com");
            // 实例化Tbaas的client对象
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 第二个参数是地域信息，根据资源所属地域填写相应的地域信息，比如广州地域的资源可以直接填写字符串ap-guangzhou，或者引用预设的常量
            TbaasClient client = new TbaasClient(cred, serviceSetting.getArea(), clientProfile);
            //获取节点信息,并构建请求对象中的节点信息
            StringBuilder peersInfo = new StringBuilder();
            for(Peers i:peers){
                peersInfo.append("{\"PeerName\":\"").append(i.getPeer()).append("\",\"OrgName\":\"").append(i.getPorgname()).append("\"}").append(",");
            }
            String params;
            if(state.equalsIgnoreCase("invoke")){
                params = "{\"Module\":\"transaction\",\"Operation\": \"invoke\",\"ClusterId\" : \"" +serviceSetting.getClusterid()+ "\"" +
                        ",\"Peers\":["+peersInfo.toString().substring(0, peersInfo.length() - 1)+"]," +
                        "\"ChannelName\" : \""+serviceSetting.getChannelname()+"\",\"ChaincodeName\" : \""+serviceSetting.getChaincode()+"\"," +
                        "\"FuncName\" : \""+funcname+"\",\"Args\" : "+args+",\"GroupName\":\""+orgname+"\",\"AsyncFlag\" : 0}";
                // 实例化一个请求对象，根据调用的接口和实际情况，可以进一步设置请求参数
                InvokeRequest req = InvokeRequest.fromJsonString(params, InvokeRequest.class);
                // 通过client对象调用想要访问的接口，需要传入请求对象
                InvokeResponse resp = client.Invoke(req);
                // 输出json格式的字符串回包
                return InvokeResponse.toJsonString(resp);
            }else{
                params = "{\"Module\":\"transaction\",\"Operation\": \"query\",\"ClusterId\" : \"" +serviceSetting.getClusterid()+ "\"" +
                        ",\"Peers\":["+peersInfo.toString().substring(0, peersInfo.length() - 1)+"]," +
                        "\"ChannelName\" : \""+serviceSetting.getChannelname()+"\",\"ChaincodeName\" : \""+serviceSetting.getChaincode()+"\"," +
                        "\"FuncName\" : \""+funcname+"\",\"Args\" : "+args+",\"GroupName\" : \""+orgname+"\"}";
                //操作同上
                QueryRequest queryRequest = QueryRequest.fromJsonString(params, QueryRequest.class);
                QueryResponse queryResponse = client.Query(queryRequest);
                return  QueryResponse.toJsonString(queryResponse);
            }

        } catch (TencentCloudSDKException e) {
            return e.toString();
        }
    }
}
