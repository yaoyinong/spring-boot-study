package com.elasticsearch.model.dto;

import com.elasticsearch.es.doc.HotelDoc;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author yaoyinong
 * @date 2022/7/25 10:02
 * @description
 */
@Data
public class HotelDTO extends HotelDoc {

    private Map<String, List<String>> highlight;

}
