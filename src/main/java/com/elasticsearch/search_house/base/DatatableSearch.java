package com.elasticsearch.search_house.base;

import org.springframework.format.annotation.DateTimeFormat;


import java.util.Date;

@lombok.Data
public class DatatableSearch {
    /**
     * Datatables 规定回显字段
     */
    private int draw;

    /**
     * Datatables 规定分页字段
     */
    private int start;
    private int length;

    private Integer status;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTimeMin;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTimeMax;

    private String city;
    private String title;
    private String direction;
    private String orderBy;
}
