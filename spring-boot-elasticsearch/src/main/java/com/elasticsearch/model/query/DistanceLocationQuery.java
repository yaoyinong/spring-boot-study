package com.elasticsearch.model.query;

import cn.hutool.core.text.CharSequenceUtil;
import lombok.Data;

/**
 * @author yaoyinong
 * @date 2022/7/25 21:33
 * @description
 */
@Data
public class DistanceLocationQuery {

    private Double latitude;

    private Double longitude;

    private String distance;

    public void setLatitude(Double latitude) {
        this.latitude = latitude != null ? latitude : 0;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude != null ? longitude : 0;
    }

    public void setDistance(String distance) {
        this.distance = CharSequenceUtil.isNotBlank(distance) ? distance : "1km";
    }
}
