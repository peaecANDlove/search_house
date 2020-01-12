package com.elasticsearch.search_house.mapper;

import com.elasticsearch.search_house.model.HouseTag;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface HouseTagMapper {

    @Select("select * from house_tag where house_id = #{id}")
    List<HouseTag> findAllByHouseId(Long id);

    @Select({
            "<script>" +
                    "select * from house_tag "
                    +"where house_id in "
                    +"<foreach item = 'item' collection = 'house_id' open = '(' separator = ',' close = ')'> "
                    +"#{item}"
                    +"</foreach>"
            +"</script>"
    })
    List<HouseTag> findAllByHouseIdIn(@Param("house_id") List<Long> house_id);

    @Insert("insert into house_tag (house_id, id, name) values (#{houseId}, #{id}, #{name}) " +
            "ON DUPLICATE KEY UPDATE " +
            "house_id = #{houseId}, name = #{name}")
    boolean houseTagSave(HouseTag houseTag);

    @Select("select * from house_tag where name = #{tag} and house_id = #{houseId}")
    HouseTag findByNameAndHouseId(@Param("tag") String tag, @Param("houseId") Long houseId);

    @Delete("delete from house_tag where id = #{houseTagId}")
    boolean deleteHouseTag(Long houseTagId);
}
