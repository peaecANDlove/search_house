package com.elasticsearch.search_house;


import com.elasticsearch.search_house.base.HouseStatus;
import com.elasticsearch.search_house.mapper.HouseDetailMapper;
import com.elasticsearch.search_house.mapper.HouseMapper;
import com.elasticsearch.search_house.mapper.HouseTagMapper;
import com.elasticsearch.search_house.mapper.SubwayStationMapper;
import com.elasticsearch.search_house.model.House;
import com.elasticsearch.search_house.model.HouseDetail;
import com.elasticsearch.search_house.service.house.AddressServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.BinaryClient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

    @Autowired
    private AddressServiceImpl addressService;

    @Autowired
    private HouseDetailMapper houseDetailMapper;

    @Autowired
    private HouseTagMapper houseTagMapper;

    @Autowired
    private HouseMapper houseMapper;

    @Autowired
    SubwayStationMapper subwayStationMapper;

    private Long s = new Long(1);

    @Test
    public void testStation(){
        System.out.println(addressService.findAllStationBySubway(s));
        //System.out.println(subwayStationMapper.findAllBySubwayId(s));
    }

    @Test
    public void testFindAllByHouseIdInDetail(){
        List<Long> houseId = new ArrayList<>();
        houseId.add(15L);
        houseId.add(16L);
        houseId.add(17L);
        houseId.add(18L);


        System.out.println(houseDetailMapper.findAllByHouseIdIn(houseId));
    }

    @Test
    public void testFindAllByHouseIdInTag(){

        List<Long> houseId = new ArrayList<>();
        houseId.add(15L);
        houseId.add(17L);

        System.out.println(houseTagMapper.findAllByHouseIdIn(houseId));

    }

    @Test
    public void findAllHouse(){
        List<House> houses = new ArrayList<>();
        houses = houseMapper.findAllHouse();

        for (House houses1: houses) {
            System.out.println(houses1);
        }
    }

    @Test
    public void findHouse(){
        Date date = new Date(2018, 06, 26 );
        System.out.println(houseMapper.findAllHouseByCreateTimeMax(date));
        System.out.println("-----------");

        System.out.println(houseMapper.findAllHouseByStatus(1));
        System.out.println("-----------");
        System.out.println(houseMapper.findAllHouseByTitle("湖光"));
    }

    @Test
    public void houseStatus() {

        System.out.println(houseMapper.findAllHouseByStatusAndCityEnName(HouseStatus.PASSES.getValue(), "bj"));

    }

}
