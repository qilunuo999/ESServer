package com.mychaincode.server.repository;

import com.mychaincode.server.entity.Certificate;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface CertificateRepository {
    @Insert("insert into certificate(id,certificate,filetype,starttime,endtime)values(#{id},#{certificate},#{filetype},#{starttime},#{endtime})")
    boolean insert(Certificate certificate);

    @Select("select * from certificate where id=#{id}")
    Certificate getById(String id);

    @Select("select id from certificate where certificate=#{certificate}")
    String getIdByName(String certificate);
}
