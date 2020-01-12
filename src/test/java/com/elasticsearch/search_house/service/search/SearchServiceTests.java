package com.elasticsearch.search_house.service.search;

import com.elasticsearch.search_house.ApplicationTests;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SearchServiceTests extends ApplicationTests {
    @Autowired
    private ISearchService searchService;

    @Test
    public void testIndex(){
        Long targetHouseId = 15L;
        boolean success = searchService.index(targetHouseId);
        Assert.assertTrue(success);
    }

    @Test
    public void testRemove(){
        Long targetHouseId = 15L;
        searchService.remove(targetHouseId);
    }
}
