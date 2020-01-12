package com.elasticsearch.search_house;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;


@SpringBootApplication
@MapperScan(value = "com.elasticsearch.search_house.mapper")
public class SearchHouseApplication {

    public static void main(String[] args) {
        SpringApplication.run(SearchHouseApplication.class, args);
    }

    @GetMapping("/")
    public String hello(){
        return "hello";
    }


}
