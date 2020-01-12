package com.elasticsearch.search_house.model;

import lombok.Data;

@Data
public class HouseDetail {

    private Long id;

    private Long houseId;

    private String description;

    private String layoutDesc;

    private String traffic;

    private String roundService;

    private int rentWay;

    private String detailAddress;

    private Long subwayLineId;

    private Long subwayStationId;

    private String subwayLineName;

    private String subwayStationName;

}
