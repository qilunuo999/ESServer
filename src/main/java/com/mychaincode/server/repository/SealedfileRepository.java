package com.mychaincode.server.repository;

import com.mychaincode.server.entity.Sealedfile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface SealedfileRepository {

    @Select("select label from sealedfile where id=#{id}")
    List<Sealedfile> findLabel(int id);

}
