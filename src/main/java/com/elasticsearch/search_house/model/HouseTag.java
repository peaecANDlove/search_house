package com.elasticsearch.search_house.model;

import lombok.Data;

@Data
public class HouseTag {

    private Long id;

    private String name;

    private Long houseId;

    public HouseTag(Long houseId, String name) {
        this.name = name;
        this.houseId = houseId;
    }
}
