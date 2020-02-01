package com.mychaincode.server.repository;

import com.mychaincode.server.entity.Sealtable;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.SessionAttribute;

@Component
@Mapper
public interface SealtableRepository {

    @Select("Select * from sealtable where sealname={sealname}")
    Sealtable getSealByName(String sealname);

    @Insert("Insert into sealtable(sealid,sealname,uid,certificateid,sealtype,info)values(#{sealid},#{sealname},#{uid},#{certificateid},#{sealtype},#{info})")
    void insert(Sealtable sealtable);
}
