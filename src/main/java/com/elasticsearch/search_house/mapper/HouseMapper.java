package com.elasticsearch.search_house.mapper;

import com.elasticsearch.search_house.base.HouseStatus;
import com.elasticsearch.search_house.model.House;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Mapper
@Repository
public interface HouseMapper {


    @Update("update house as house set house.cover = #{cover} where house.id = #{id}")
    void updateCover(@Param(value = "id") Long id, @Param(value = "cover") String cover);

    @Update("update house as house set house.status = #{status} where house.id = #{id}")
    void updateStatus(@Param(value = "id") Long id, @Param(value = "status") int status);

    @Update("update house as house set house.watch_times = house.watch_times + 1 where house.id = #{id}")
    void updateWatchTime(@Param(value = "id") Long houseId);

    @Insert("insert into house (id, title, price, area, room, floor, total_floor, watch_times, " +
            "build_year, status, create_time, last_update_time, city_en_name, region_en_name, " +
            "cover, direction, distance_to_subway, parlour, district, admin_id, bathroom, street) values " +
            "(#{id}, #{title}, #{price}, #{area}, #{room}, #{floor}, #{totalFloor}, #{watchTimes}, " +
            "#{buildYear}, #{status}, #{createTime}, #{lastUpdateTime}, #{cityEnName}, #{regionEnName}, " +
            "#{cover}, #{direction}, #{distanceToSubway}, #{parlour}, #{district}, #{adminId}, #{bathroom}, #{street}) " +
            "ON DUPLICATE KEY UPDATE " +
            "title = #{title}, price = #{price}, area = #{area}, room = #{room}, floor = #{floor}, " +
            "total_floor = #{totalFloor}, watch_times = #{watchTimes}, build_year=#{buildYear}, " +
            "status = #{status}, create_time = #{createTime}, last_update_time = #{lastUpdateTime}, " +
            "city_en_name = #{cityEnName}, region_en_name = #{regionEnName}, cover = #{cover}, " +
            "direction = #{direction}, distance_to_subway = #{distanceToSubway}, parlour = #{parlour}, " +
            "district = #{district}, admin_id = {admin_id}, bathroom = #{bathroom}, street = #{street}")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Long  houseSave(House house);

    @Select("select * from house")
    List<House> findAllHouse();

    @Select("select * from house where city_en_name = #{city}")
    List<House> findAllHouseByCity(String city);

    @Select("select * from house where status =#{status}")
    List<House> findAllHouseByStatus(Integer status);

    @Select("select * from house where create_time >= #{createTimeMin}")
    List<House> findAllHouseByCreateTimeMin(Date createTimeMin);

    @Select("select * from house where create_time <= #{createTimeMax}")
    List<House> findAllHouseByCreateTimeMax(Date createTimeMax);

    @Select("select * from house where title like CONCAT('%',#{title},'%')")
    List<House> findAllHouseByTitle(String title);

    @Select("select * from house where id =#{id}")
    House findOne(Long id);

    @Select("select * from house where status = #{status} and city_en_name = #{cityEnName}")
    List<House> findAllHouseByStatusAndCityEnName(@Param("status") int status, @Param("cityEnName") String cityEnName);

}
