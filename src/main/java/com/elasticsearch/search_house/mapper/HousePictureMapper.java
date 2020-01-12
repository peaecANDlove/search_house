package com.elasticsearch.search_house.mapper;

import com.elasticsearch.search_house.model.HousePicture;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface HousePictureMapper {

    @Select("select * from house_picture where house_id = #{id}")
    List<HousePicture> findAllByHouseId(Long id);
}
