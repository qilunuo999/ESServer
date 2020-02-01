package com.mychaincode.server.repository;

import com.mychaincode.server.entity.ServiceSetting;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface ServiceSettingRepository {
    @Insert("insert into servicesetting(scriptid,scriptkey,area,channelname,chaincode,clusterid)values(#{scriptid},#{scriptkey},#{area}," +
            "#{channelname},#{chaincode},#{clusterid})on duplicate key update scriptid=#{scriptid},scriptkey=#{scriptkey}," +
            "area=#{area},channelname=#{channelname},chaincode=#{chaincode},clusterid=#{clusterid}")
    void insert(ServiceSetting serviceSetting);
    @Select("select * from servicesetting")
    ServiceSetting getServiceSetting();
}
