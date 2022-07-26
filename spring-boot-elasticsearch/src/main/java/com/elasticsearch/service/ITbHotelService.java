package com.elasticsearch.service;

import com.elasticsearch.entity.TbHotel;
import com.baomidou.mybatisplus.extension.service.IService;
import com.elasticsearch.model.dto.HotelDTO;
import com.elasticsearch.model.query.DistanceLocationQuery;
import com.elasticsearch.model.query.HotelFilterQuery;
import com.elasticsearch.model.query.HotelListQuery;
import com.elasticsearch.model.result.PageResult;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yaoyinong
 * @since 2022-07-21
 */
public interface ITbHotelService extends IService<TbHotel> {

    PageResult list(HotelListQuery query);

    List<HotelDTO> distanceLocation(DistanceLocationQuery query);

    Map<String, Set<String>> filters(HotelFilterQuery query);

}
