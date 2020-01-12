package com.elasticsearch.search_house.model;

import lombok.Data;

@Data
public class HousePicture {

    private Long id;

    private Long houseId;

    private String path;

    private String cdnPrefix;

    private int width;

    private int height;

    private String location;
}
