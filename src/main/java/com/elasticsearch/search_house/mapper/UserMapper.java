package com.elasticsearch.search_house.mapper;

import com.elasticsearch.search_house.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {

    @Select("Select * from user where name=#{username}")
    User findUserByUsername(String username);
}
