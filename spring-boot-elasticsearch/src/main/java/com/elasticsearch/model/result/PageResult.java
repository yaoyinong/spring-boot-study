package com.elasticsearch.model.result;

import com.elasticsearch.model.dto.HotelDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yaoyinong
 * @date 2022/7/25 19:21
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult {

    private Long total;

    private List<HotelDTO> hotelDTOList;

}
