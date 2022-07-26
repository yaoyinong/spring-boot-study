package com.elasticsearch.model.query;

import lombok.Data;
import org.elasticsearch.common.geo.GeoPoint;

/**
 * @author yaoyinong
 * @date 2022/7/25 19:23
 * @description
 */
@Data
public class HotelFilterQuery {

    /**
     * all查询
     */
    private String key;

    /**
     * 城市
     * 精确匹配
     */
    private String city;

    /**
     * 品牌
     * 精确匹配
     */
    private String brand;

    /**
     * 星级
     * 精确匹配
     */
    private String starName;

    /**
     * 最低价格
     * 精确匹配
     */
    private Integer minPrice;

    /**
     * 最高价格
     * 精确匹配
     */
    private Integer maxPrice;

}
