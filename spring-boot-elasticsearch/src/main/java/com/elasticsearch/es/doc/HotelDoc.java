package com.elasticsearch.es.doc;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;

/**
 * @author yaoyinong
 * @date 2022/7/21 21:27
 * @description 索引映射
 */
@Document(indexName = "hotel",createIndex = true)
@Data
public class HotelDoc {

    /**
     * 酒店id
     */
    @Field(type = FieldType.Keyword)
    private Long id;

    /**
     * 酒店名称
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", copyTo = "all")
    private String name;

    /**
     * 酒店地址
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", copyTo = "all")
    private String address;

    /**
     * 酒店价格
     */
    @Field(type = FieldType.Integer)
    private Integer price;

    /**
     * 酒店评分
     */
    @Field(type = FieldType.Integer)
    private Integer score;

    /**
     * 酒店品牌
     */
    @Field(type = FieldType.Keyword, copyTo = "all")
    private String brand;

    /**
     * 所在城市
     */
    @Field(type = FieldType.Keyword)
    private String city;

    /**
     * 酒店星级，1星到5星，1钻到5钻
     */
    @Field(type = FieldType.Keyword)
    private String starName;

    /**
     * 商圈
     */
    @Field(type = FieldType.Keyword, copyTo = "all")
    private String business;

    /**
     * 经纬度
     */
    @GeoPointField
    private GeoPointDoc location;

    /**
     * 酒店图片
     */
    @Field(type = FieldType.Keyword)
    private String pic;

    /**
     * 聚合搜索字段
     */
    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String all;

}
