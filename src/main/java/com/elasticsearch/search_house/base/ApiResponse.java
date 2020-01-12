package com.elasticsearch.search_house.base;

import lombok.Data;

/**
 * API格式封装
 */
@Data
public class ApiResponse {
    private int code;
    private String message;
    private Object data;
    private boolean more;

    public ApiResponse(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ApiResponse() {
        this.code = Status.SUCCESS.getCode();
        this.message = Status.SUCCESS.getStandardMessage();
    }

    public static ApiResponse ofMessage(int code, String message){
        return new ApiResponse(code, message, null);
    }

    public static ApiResponse OfSuccess(Object data){
        return new ApiResponse(Status.SUCCESS.getCode(), Status.SUCCESS.getStandardMessage(), data);
    }

    public static ApiResponse ofStatus(Status status){
        return  new ApiResponse(status.getCode(), status.getStandardMessage(), null);
    }

    public enum Status{
        SUCCESS(200,"OK"),
        BAD_REQUEST(400,"Bad Request"),
        INTERNAL_SERVER_ERROR(500,"Unknown Internal Error"),
        Not_FOUND(404, "Not Found"),
        NOT_VALID_PARAM(40005,"Not Valid Params"),
        NOT_SUPPORT_OPEARATION(40006,"Operation not supported"),
        NOT_LOGIN(50000,"Not Login");


        private  int code;
        private String standardMessage;

        Status(int code, String standerMessage) {
            this.code = code;
            this.standardMessage = standerMessage;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getStandardMessage() {
            return standardMessage;
        }

        public void setStandardMessage(String standardMessage) {
            this.standardMessage = standardMessage;
        }

        public class Not_FOUND {
        }
    }
}
