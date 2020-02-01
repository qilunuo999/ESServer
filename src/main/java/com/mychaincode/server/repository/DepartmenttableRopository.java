package com.mychaincode.server.repository;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface DepartmenttableRopository {

    //添加用户
    @Insert("insert into departmenttable(department) values(#{department})")
    void insert(String department);

    //删除用户
    @Delete("delete from departmenttable where department=#{department}")
    void delete(String department);

}
