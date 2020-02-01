package com.mychaincode.server.repository;

import com.mychaincode.server.entity.Certificate;
import com.mychaincode.server.entity.Certificateaudit;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface CertificateauditRepository {

    @Insert("insert ignore into certificateaudit(certificate,uid,cid,filetype,state)values(#{certificate},#{uid},#{cid},#{filetype},#{state})")
    boolean insert(Certificateaudit certificateaudit);

    @Select("select * from certificateaudit where state='未认证'")
    List<Certificateaudit> getByPageNo();

    @Select("select * from certificateaudit where id=#{id}")
    Certificateaudit getById(int id);

    @Update("update certificateaudit set state=#{state} where id=#{id}")
    boolean update(String state, int id);
}
