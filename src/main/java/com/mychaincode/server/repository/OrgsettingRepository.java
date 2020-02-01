package com.mychaincode.server.repository;

import com.mychaincode.server.entity.Orgsetting;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface OrgsettingRepository {
    @Select("select * from orgsetting")
    List<Orgsetting> getOrgInfo();
    @Insert("insert into orgsetting(orgname)values(#{orgname})")
    void insertInfo(Orgsetting orgsetting);
}
