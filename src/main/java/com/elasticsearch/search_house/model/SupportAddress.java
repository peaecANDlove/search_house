package com.elasticsearch.search_house.model;


import lombok.Data;

@Data
public class SupportAddress {

    private Long id;

    //上一级行政单位
    private String belongTo;

    private String enName;

    private String cnName;

    private String level;

    /**
     *行政级别定义
     */
    public enum Level {
        CITY("city"),
        REGION("region");

        private String value;

        Level(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static Level of(String value) {
            for (Level level : Level.values()){
                if (level.getValue().equals(value)){
                    return level;
                }
            }
            throw new IllegalArgumentException();
        }
    }

}
