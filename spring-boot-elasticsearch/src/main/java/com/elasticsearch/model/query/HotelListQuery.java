package com.elasticsearch.model.query;

import lombok.Data;

/**
 * @author yaoyinong
 * @date 2022/7/25 19:23
 * @description
 */
@Data
public class HotelListQuery extends PageBaseQuery {

    private String key;

    private String city;

    private String brand;

    private String starName;

    private Integer minPrice;

    private Integer maxPrice;

}
