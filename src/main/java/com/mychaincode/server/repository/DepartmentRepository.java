package com.mychaincode.server.repository;

import com.mychaincode.server.entity.Department;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface DepartmentRepository {
    @Select("select department from department where department<>'外部'")
    List<String> getDepartmentInfo();

    @Select("select dorgname from department where department=#{department}")
    String getOrgname(Department department);

    @Insert("insert into department(department, dorgname)values(#{department},#{dorgname})")
    void insert(Department info);
}
