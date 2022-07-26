package com.elasticsearch.service.impl;

import cn.hutool.core.text.CharSequenceUtil;
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
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.GeoDistanceSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
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
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        HighlightBuilder highlightBuilder = new HighlightBuilder().requireFieldMatch(false).preTags("<light>").postTags("</light>");
        if (CharSequenceUtil.isNotBlank(query.getKey())) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("all", query.getKey()));
            highlightBuilder.field("name");
        }
        if (CharSequenceUtil.isNotBlank(query.getCity())) {
            boolQueryBuilder.must(QueryBuilders.termQuery("city", query.getCity()));
        }
        if (CharSequenceUtil.isNotBlank(query.getBrand())) {
            boolQueryBuilder.must(QueryBuilders.termQuery("brand", query.getBrand()));
        }
        if (CharSequenceUtil.isNotBlank(query.getStarName())) {
            boolQueryBuilder.must(QueryBuilders.termQuery("starName", query.getStarName()));
        }
        if (query.getMinPrice() != null || query.getMaxPrice() != null) {
            boolQueryBuilder.must(QueryBuilders.rangeQuery("price").gte(query.getMinPrice()).lte(query.getMaxPrice()));
        }
        //设置分数权重
        FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(boolQueryBuilder, new FunctionScoreQueryBuilder.FilterFunctionBuilder[]{
                new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                        QueryBuilders.termQuery("isAd", true),
                        ScoreFunctionBuilders.weightFactorFunction(10)
                )
        });
        functionScoreQueryBuilder.scoreMode(FunctionScoreQuery.ScoreMode.SUM);
        //分页
        PageRequest page = PageRequest.of(query.getPage() - 1, query.getSize());
        //排序
        SortBuilder<?> sortBuilder = new FieldSortBuilder(query.getSortBy()).order(SortOrder.fromString(query.getDirection()));
        boolean isDistance = false;
        if ("distance".equals(query.getSortBy())) {
            //位置排序
            isDistance = true;
            sortBuilder = new GeoDistanceSortBuilder("location", query.getGeoPoint())
                    .order(SortOrder.fromString(query.getDirection()))
                    .unit(DistanceUnit.KILOMETERS);
        }
        //拼装
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(functionScoreQueryBuilder)
                .withHighlightBuilder(highlightBuilder)
                .withPageable(page)
                .withSorts(sortBuilder)
                .build();
        SearchHits<HotelDoc> search = elasticsearchRestTemplate.search(searchQuery, HotelDoc.class);
        return new PageResult(search.getTotalHits(), responseModel(search, isDistance));
    }

    /**
     * 拼装返回对象
     */
    private List<HotelDTO> responseModel(SearchHits<?> searchHits, boolean isDistance) {
        return searchHits.get().map(hotel -> {
            HotelDTO hotelDTO = new HotelDTO();
            BeanUtils.copyProperties(hotel.getContent(), hotelDTO);
            Map<String, List<String>> highlightFields = hotel.getHighlightFields();
            if (highlightFields.size() > 0) {
                for (Map.Entry<String, List<String>> entry : highlightFields.entrySet()) {
                    BeanMap beanMap = BeanMap.create(hotelDTO);
                    beanMap.put(entry.getKey(), highlightFields.get(entry.getKey()).get(0));
                    hotelDTO = (HotelDTO) beanMap.getBean();
                }
            }
            if (isDistance) {
                hotelDTO.setDistance(hotel.getSortValues().get(0) + "");
            }
            return hotelDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<HotelDTO> distanceLocation(DistanceLocationQuery query) {
        GeoPoint point = new GeoPoint(query.getLatitude(), query.getLongitude());
        GeoDistanceQueryBuilder queryBuilder = QueryBuilders.geoDistanceQuery("location");
        queryBuilder.distance("2km");
        queryBuilder.point(point);
        GeoDistanceSortBuilder sortBuilder = new GeoDistanceSortBuilder("location", point).order(SortOrder.ASC);
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder).withSorts(sortBuilder).build();
        SearchHits<HotelDoc> search = elasticsearchRestTemplate.search(searchQuery, HotelDoc.class);
        return responseModel(search, true);
    }

}
