package com.elasticsearch.es.doc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yaoyinong
 * @date 2022/7/22 00:13
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeoPointDoc {

    /**
     * 纬度
     */
    private String lat;

    /**
     * 经度
     */
    private String lon;

}
