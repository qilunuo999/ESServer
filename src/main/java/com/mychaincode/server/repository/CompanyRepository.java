package com.mychaincode.server.repository;

import com.mychaincode.server.entity.Company;
import com.mychaincode.server.entity.Users;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface CompanyRepository {

    @Select("select id from company where email=#{email}")
    int getIdByEmail(String email);

    @Select("select company from company where id=#{id}")
    String getCompanyById(int id);

    @Select("select * from company where state='未认证'")
    List<Company> getUnderApprovalInfoList();

    @Select("select * from company where company=#{company}")
    Company getInfoByEmail(String company);

    @Select("select * from company where id=#{id}")
    Company getInfoById(int id);

    @Insert("insert ignore into company(email,company,creditcode,verifyinfo,license,state) value(#{email},#{company},#{creditcode},#{verifyinfo},#{license},#{state})")
    boolean insert(Company company);

//    @Update("update company set state=#{state} where id=#{id}")
//    boolean update(String state, int id);

    @Update("update company set state=#{state} where email=#{email}")
    boolean update(String state,String email);

}
