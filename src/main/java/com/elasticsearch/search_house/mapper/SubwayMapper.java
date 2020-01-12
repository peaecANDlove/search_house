package com.elasticsearch.search_house.mapper;

import com.elasticsearch.search_house.model.Subway;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SubwayMapper {

    @Select("select * from subway where city_en_name = #{cityEnName}")
    List<Subway> findAllByCityEnName(String cityEnName);

    /**
     * 通过 Id 查询一条地铁线路
     * @return
     */
    @Select("select * from subway where id = #{id}")
    Subway findOneById(Long id);
}
