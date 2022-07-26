package com.elasticsearch.model.dto;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Data;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;


/**
 * @author yaoyinong
 * @date 2022/7/25 10:02
 * @description
 */
@Data
public class HotelDTO {

    /**
     * 酒店id
     */
    private Long id;

    /**
     * 酒店名称
     */
    private String name;

    /**
     * 酒店地址
     */
    private String address;

    /**
     * 酒店价格
     */
    private Integer price;

    /**
     * 酒店评分
     */
    private Integer score;

    /**
     * 酒店品牌
     */
    private String brand;

    /**
     * 所在城市
     */
    private String city;

    /**
     * 酒店星级，1星到5星，1钻到5钻
     */
    private String starName;

    /**
     * 商圈
     */
    private String business;

    /**
     * 经纬度
     */
    private GeoPoint location;

    /**
     * 酒店图片
     */
    private String pic;

    /**
     * 距离
     */
    private String distance;

    /**
     * 是否为广告
     */
    private Boolean isAd;

}
