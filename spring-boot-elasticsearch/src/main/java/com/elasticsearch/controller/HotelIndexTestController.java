package com.elasticsearch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author yaoyinong
 * @date 2022/7/21 21:35
 * @description
 */
@RestController
public class HotelIndexTestController {

    @Autowired
    private ElasticsearchRestTemplate template;

    @GetMapping("/getIndex")
    public Map<String,Object> getIndex() {
        return template.indexOps(IndexCoordinates.of("hotel")).getMapping();
    }

}
