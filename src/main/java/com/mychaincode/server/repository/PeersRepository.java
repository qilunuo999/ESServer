package com.mychaincode.server.repository;

import com.mychaincode.server.entity.Peers;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface PeersRepository {
    @Select("select * from peers")
    List<Peers> getPeersInfo();
    @Insert("insert into peers(peer,porgname)values(#{peer},#{porgname})")
    void insert(Peers peers);
}
