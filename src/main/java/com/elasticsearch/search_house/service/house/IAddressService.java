package com.elasticsearch.search_house.service.house;

import com.elasticsearch.search_house.dto.SubwayDTO;
import com.elasticsearch.search_house.dto.SubwayStationDTO;
import com.elasticsearch.search_house.dto.SupportAddressDTO;
import com.elasticsearch.search_house.model.SupportAddress;
import com.elasticsearch.search_house.service.ServiceMultiResult;
import com.elasticsearch.search_house.service.userImpl.ServiceResult;


import java.util.List;
import java.util.Map;

/**
 * 地址服务接口
 */
public interface IAddressService {

    /**
     * 获取支持所有城市列表
     * @return
     */
    ServiceMultiResult<SupportAddressDTO> findAllCities();

    /**
     * 依据英文简写获取具体区域信息
     */
    Map<SupportAddress.Level, SupportAddressDTO> findCityAndRegion(String cityEnName, String regionEnName);

    /**
     * 依据城市英文简写获取该城市所有支持区域信息
     */
    ServiceMultiResult<SupportAddressDTO> findAllRegionsByCityName(String cityName);

    /**
     * 获取该城市所有地铁线路
     */
    List<SubwayDTO> findAllSubwayByCity(String cityEnName);

    /**
     * 获取地铁线路所有站点
     * @param subwayId
     * @return
     */
    List<SubwayStationDTO> findAllStationBySubway(Long subwayId);


    /**
     * 获取地铁站点信息
     * @param subwayId
     * @return
     */
    ServiceResult<SubwayDTO> findSubway(Long subwayId);

    ServiceResult<SubwayStationDTO> findSubwayStation(Long subwayStationId);

    /**
     * 依据城市英文简写获取详细信息
     * @param cityEnName
     * @return
     */
    ServiceResult<SupportAddressDTO> findCity(String cityEnName);

}
