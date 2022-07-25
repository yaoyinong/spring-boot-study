package com.elasticsearch.controller;

import com.elasticsearch.model.dto.HotelDTO;
import com.elasticsearch.model.query.DistanceLocationQuery;
import com.elasticsearch.model.query.HotelListQuery;
import com.elasticsearch.model.result.PageResult;
import com.elasticsearch.service.ITbHotelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author yaoyinong
 * @date 2022/7/21 21:35
 * @description
 */
@Slf4j
@RestController
@RequestMapping("/hotel")
public class HotelController {

    @Autowired
    private ITbHotelService hotelService;

    /**
     * 搜索、分页
     */
    @GetMapping("/list")
    public PageResult list(@RequestBody HotelListQuery query) {
        return hotelService.list(query);
    }

    /**
     * 查询附近的酒店
     * 范围查询（圆心）
     */
    @GetMapping("/distanceLocation")
    public List<HotelDTO> distanceLocation(@RequestBody DistanceLocationQuery query) {
        return hotelService.distanceLocation(query);
    }

}
