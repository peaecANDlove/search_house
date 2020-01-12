package com.elasticsearch.search_house.service.house;

import com.alibaba.druid.sql.visitor.functions.If;
import com.elasticsearch.search_house.dto.SubwayDTO;
import com.elasticsearch.search_house.dto.SubwayStationDTO;
import com.elasticsearch.search_house.dto.SupportAddressDTO;
import com.elasticsearch.search_house.mapper.SubwayMapper;
import com.elasticsearch.search_house.mapper.SubwayStationMapper;
import com.elasticsearch.search_house.mapper.SupportAddressMapper;
import com.elasticsearch.search_house.model.Subway;
import com.elasticsearch.search_house.model.SubwayStation;
import com.elasticsearch.search_house.model.SupportAddress;
import com.elasticsearch.search_house.service.ServiceMultiResult;
import com.elasticsearch.search_house.service.userImpl.ServiceResult;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.transform.Result;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AddressServiceImpl implements IAddressService {

    @Autowired
    private SupportAddressMapper supportAddressMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SubwayMapper subwayMapper;

    @Autowired
    private SubwayStationMapper subwayStationMapper;

    @Override
    public ServiceMultiResult<SupportAddressDTO> findAllCities() {
        List<SupportAddress> addresses = supportAddressMapper.findAllByLevel(SupportAddress.Level.CITY.getValue());

        List<SupportAddressDTO> addressDTOS = new ArrayList<>();

        for (SupportAddress supportAddress : addresses) {
            SupportAddressDTO target = modelMapper.map(supportAddress, SupportAddressDTO.class);

            addressDTOS.add(target);
        }

        return new ServiceMultiResult<>(addressDTOS.size(), addressDTOS);
    }

    @Override
    public Map<SupportAddress.Level, SupportAddressDTO> findCityAndRegion(String cityEnName, String regionEnName) {
        Map<SupportAddress.Level, SupportAddressDTO> result = new HashMap<>();

        SupportAddress city = supportAddressMapper.findByEnNameAndLevel(cityEnName, SupportAddress.Level.CITY.getValue());
        SupportAddress region = supportAddressMapper.findByEnNameAndBelongTo(regionEnName, city.getEnName());

        result.put(SupportAddress.Level.CITY, modelMapper.map(city, SupportAddressDTO.class));
        result.put(SupportAddress.Level.REGION, modelMapper.map(region, SupportAddressDTO.class));
        return result;
    }

    @Override
    public ServiceMultiResult<SupportAddressDTO> findAllRegionsByCityName(String cityName) {
        if (cityName == null){
            return new ServiceMultiResult<>(0,null);
        }

        List<SupportAddressDTO> result = new ArrayList<>();

        List<SupportAddress> regions = supportAddressMapper.findAllByLevelAndBelongTo(
                SupportAddress.
                Level.REGION.getValue(), cityName);

        for (SupportAddress region : regions) {
            result.add(modelMapper.map(region, SupportAddressDTO.class));
        }

        return new ServiceMultiResult<>(regions.size(), result);
    }

    @Override
    public List<SubwayDTO> findAllSubwayByCity(String cityEnName) {
        List<SubwayDTO> result = new ArrayList<>();
        List<Subway> subways = subwayMapper.findAllByCityEnName(cityEnName);
        if (subways.isEmpty()){
            return result;
        }

        subways.forEach(subway -> result.add(modelMapper.map(subway, SubwayDTO.class)));

        return result;
    }

    @Override
    public List<SubwayStationDTO> findAllStationBySubway(Long subwayId) {
        List<SubwayStationDTO> result = new ArrayList<>();
        List<SubwayStation> stations = subwayStationMapper.findAllBySubwayId(subwayId);
        if (stations.isEmpty()){
            return result;
        }

        stations.forEach(station -> result.add(modelMapper.map(station, SubwayStationDTO.class)));
        return result;
    }

    @Override
    public ServiceResult<SubwayDTO> findSubway(Long subwayId) {
        if (subwayId == null) {
            return ServiceResult.notFound();
        }

        Subway subway = subwayMapper.findOneById(subwayId);
        if (subway == null) {
            return ServiceResult.notFound();
        }

        return ServiceResult.of(modelMapper.map(subway, SubwayDTO.class));
    }

    @Override
    public ServiceResult<SubwayStationDTO> findSubwayStation(Long subwayStationId) {

        if (subwayStationId == null) {
            return ServiceResult.notFound();
        }

        SubwayStation subwayStation = subwayStationMapper.findOneById(subwayStationId);
        if (subwayStation == null) {
            return ServiceResult.notFound();
        }

        return ServiceResult.of(modelMapper.map(subwayStation, SubwayStationDTO.class));
    }

    @Override
    public ServiceResult<SupportAddressDTO> findCity(String cityEnName) {
        if (cityEnName == null) {
            return ServiceResult.notFound();
        }

        SupportAddress supportAddress = supportAddressMapper.findByEnNameAndLevel(cityEnName, SupportAddress.Level.CITY.getValue());
        if (supportAddress == null) {
            return ServiceResult.notFound();
        }

        SupportAddressDTO addressDTO = modelMapper.map(supportAddress, SupportAddressDTO.class);
        return ServiceResult.of(addressDTO);
    }
}
