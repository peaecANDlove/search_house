package com.elasticsearch.search_house.mapper;

import com.elasticsearch.search_house.model.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户角色查询
 */

@Mapper
@Repository
public interface RoleMapper {

    @Select("select * from role where user_Id=#{userId}")
    List<Role> findRolesByUserId(Long userId);
}
