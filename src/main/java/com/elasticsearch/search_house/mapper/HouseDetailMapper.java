package com.elasticsearch.search_house.mapper;

import com.elasticsearch.search_house.model.House;
import com.elasticsearch.search_house.model.HouseDetail;
import com.elasticsearch.search_house.model.HouseTag;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface HouseDetailMapper {

    @Select("select * from house_detail where house_id = #{houseId}")
    HouseDetail findByHouseId(Long houseId);

    @Select({
            "<script>"
                    +"select * from house_detail  "
                    +"where house_id in"
                    +"<foreach item='item' collection='house_id' open='(' separator=',' close=')'>"
                    +"#{item}"
                    +"</foreach>"
            +"</script>"
    })
    List<HouseDetail> findAllByHouseIdIn(@Param("house_id") List<Long> house_id);

    @Insert("insert into house_detail (id, description, layout_desc, traffic, round_service, rent_way, " +
            "address, subway_line_id, subway_line_name, subway_station_id, subway_station_name, house_id) values" +
            "(#{id}, #{description}, #{layoutDesc}, #{traffic}, #{roundService}, #{rentWay}, #{detailAddress}, #{subwayLineId}, " +
            "#{subwayLineName}, #{subwayStationId}, #{subwayStationName}, #{houseId}) " +
            "ON DUPLICATE KEY UPDATE " +
            "description = #{description}, layout_desc = #{layout_desc}, traffic = #{traffic}, round_service = #{roundService}, " +
            "rent_way = #{rentWay}, address = #{address}, subway_line_id = #{subwayLineId}, subway_station_id = #{subwayStationId}, " +
            "house_id = #{houseId}")
    boolean houseDetailSave(HouseDetail houseDetail);

    @Select("select * from house_tag where id = #{id}")
    List<HouseTag> findAllByHouseId(Long id);
}
