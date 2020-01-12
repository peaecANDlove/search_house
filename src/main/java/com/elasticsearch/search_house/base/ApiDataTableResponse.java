package com.elasticsearch.search_house.base;

import lombok.Data;

/**
 * DataTable 响应结构
 */
@Data
public class ApiDataTableResponse extends ApiResponse {

    private int draw;
    private long recordsTotal;
    private long recordsFiltered;

    public ApiDataTableResponse(ApiResponse.Status status) {
        this(status.getCode(), status.getStandardMessage(), null);
    }

    public ApiDataTableResponse(int code, String message, Object data) {
        super(code, message, data);
    }

}
