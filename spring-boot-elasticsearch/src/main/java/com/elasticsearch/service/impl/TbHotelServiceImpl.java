package com.elasticsearch.service.impl;

import cn.hutool.core.text.CharSequenceUtil;
import co.elastic.clients.elasticsearch._types.GeoDistanceSort;
import co.elastic.clients.elasticsearch._types.SortOrder;
import com.elasticsearch.entity.TbHotel;
import com.elasticsearch.es.doc.HotelDoc;
import com.elasticsearch.mapper.TbHotelMapper;
import com.elasticsearch.model.dto.HotelDTO;
import com.elasticsearch.model.query.DistanceLocationQuery;
import com.elasticsearch.model.query.HotelListQuery;
import com.elasticsearch.model.result.PageResult;
import com.elasticsearch.service.ITbHotelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yaoyinong
 * @since 2022-07-21
 */
@Slf4j
@Service
public class TbHotelServiceImpl extends ServiceImpl<TbHotelMapper, TbHotel> implements ITbHotelService {

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Override
    public PageResult list(HotelListQuery query) {
        try {
            Criteria criteria = new Criteria();
            if (CharSequenceUtil.isNotBlank(query.getKey())) {
                criteria.and(new Criteria("all").matches(query.getKey()));
            }
            if (CharSequenceUtil.isNotBlank(query.getCity())) {
                criteria.and(new Criteria("city").matches(query.getCity()));
            }
            if (CharSequenceUtil.isNotBlank(query.getBrand())) {
                criteria.and(new Criteria("brand").matches(query.getBrand()));
            }
            if (CharSequenceUtil.isNotBlank(query.getStarName())) {
                criteria.and(new Criteria("starName").matches(query.getStarName()));
            }
            if (query.getMinPrice() != null || query.getMaxPrice() != null) {
                criteria.and(new Criteria("price").between(query.getMinPrice(), query.getMaxPrice()));
            }
            CriteriaQuery criteriaQuery = new CriteriaQuery(criteria);
            criteriaQuery.setPageable(Pageable.ofSize(query.getSize()).withPage(query.getPage() - 1));
            if (CharSequenceUtil.isNotBlank(query.getSortBy())) {
                criteriaQuery.addSort(Sort.by(Sort.Direction.fromString(query.getDirection()),query.getSortBy()));
            }
            SearchHits<HotelDoc> search = elasticsearchRestTemplate.search(criteriaQuery, HotelDoc.class);
            new PageResult(search.getTotalHits(), responseModel(search));
        } catch (Exception e) {
            log.error("查询列表发生错误：{}", e.getLocalizedMessage(), e);
        }
        return new PageResult();
    }

    /**
     * 拼装返回对象
     */
    private List<HotelDTO> responseModel(SearchHits<?> searchHits) {
        return searchHits.get().map(hotel -> {
            HotelDTO hotelDTO = new HotelDTO();
            BeanUtils.copyProperties(hotel.getContent(), hotelDTO);
            hotelDTO.setHighlight(hotel.getHighlightFields());
            return hotelDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<HotelDTO> distanceLocation(DistanceLocationQuery query) {
        GeoPoint point = new GeoPoint(query.getLatitude(), query.getLongitude());
        Criteria criteria = new Criteria("location").within(point, query.getDistance());
        CriteriaQuery criteriaQuery = new CriteriaQuery(criteria);
        SearchHits<HotelDoc> search = elasticsearchRestTemplate.search(criteriaQuery, HotelDoc.class);
        return responseModel(search);
    }

}
