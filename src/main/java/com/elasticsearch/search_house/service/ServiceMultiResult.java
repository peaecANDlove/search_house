package com.elasticsearch.search_house.service;

import lombok.Data;

import java.util.List;

@Data
public class ServiceMultiResult<T> {

    private long total;

    private List<T> result;

    public ServiceMultiResult(long total, List<T> result) {
        this.total = total;
        this.result = result;
    }

    public int getResultSize(){
        if (this.result == null){
            return 0;
        }

        return this.result.size();
    }
}
