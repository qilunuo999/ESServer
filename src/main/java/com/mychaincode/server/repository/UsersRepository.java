package com.mychaincode.server.repository;

import com.mychaincode.server.entity.Users;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface UsersRepository {
    @Select("select * from users where username=#{username}")
    Users getByUsername(String username);

    //管理员查询用户表
    @Select("select * from users where state=#{state}")
    List<Users> employees(String state);

    //利用用户账号从数据库user表中查询用户信息
    @Select("select * from users where email=#{email}")
    Users getByEmail(String email);

    //删除用户
    @Update("update users set state=#{state} where email=#{email}")
    void delete(String email,String state);

    //添加用户
    @Insert("insert into users(username,passwd,email,department,cellphone,power,isverify,state) values(#{username}," +
            "#{passwd},#{email},#{department},#{cellphone},#{power},#{isverify},#{state})")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    void insert(Users user);

    @Update("update users set username=#{username},cellphone=#{cellphone},department=#{department},power=#{power} where email=#{email}")
    void updateAccountInfo(Users userInfo);

    //传入用户id修改密码
    @Update("update users set passwd=#{passwd} where email=#{email}")
    void updatePassword(Users user);

    @Select("select username from users where id=#{id}")
    String getNameById(int id);

    @Select("select email from users where id=#{id}")
    String getEmailById(int id);

    @Select("select * from users where email=#{email}")
    String getIdByEmail(String email);

    @Select("select username from users where email=#{email}")
    String getNameByEmail(String email);

}
