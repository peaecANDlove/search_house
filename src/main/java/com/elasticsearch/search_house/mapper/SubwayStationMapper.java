package com.elasticsearch.search_house.mapper;

import com.elasticsearch.search_house.model.SubwayStation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SubwayStationMapper {

    @Select("select * from subway_station where subway_id = #{subwayId}")
    List<SubwayStation> findAllBySubwayId(Long subwayId);

    @Select("select * from subway_station where id = #{SubwayStationId}")
    SubwayStation findOneById(Long SubwayStationId);
}
