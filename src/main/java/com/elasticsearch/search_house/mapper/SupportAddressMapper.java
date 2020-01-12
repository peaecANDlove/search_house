package com.elasticsearch.search_house.mapper;

import com.elasticsearch.search_house.model.SupportAddress;
import com.elasticsearch.search_house.service.ServiceMultiResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SupportAddressMapper {

    /**
     * 获取所以对应行政级别的信息
     * @param level
     * @return
     */
    @Select("select * from support_address where level = #{level}")
    List<SupportAddress> findAllByLevel(String level);

    @Select("select * from support_address where en_name = #{enName} and level = #{level}")
    SupportAddress findByEnNameAndLevel(@Param("enName") String enName, @Param("level") String level);

    @Select("select * from support_address where en_name = #{enName} and belong_to = #{belongTo}")
    SupportAddress findByEnNameAndBelongTo(@Param("enName")String enName, @Param("belongTo")String belongTo);

    @Select("select * from support_address where level = #{level} and belong_to = #{belongTo}")
    List<SupportAddress> findAllByLevelAndBelongTo(@Param("level") String level, @Param("belongTo") String belongTo);

}
