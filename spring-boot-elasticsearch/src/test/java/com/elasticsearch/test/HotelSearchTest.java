package com.elasticsearch.test;

import com.alibaba.fastjson.JSON;
import com.elasticsearch.model.dto.HotelDTO;
import com.elasticsearch.es.doc.HotelDoc;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoBoundingBoxQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.GeoDistanceSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.AggregationsContainer;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchScrollHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author yaoyinong
 * @date 2022/7/21 23:54
 * @description 查询测试
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class HotelSearchTest {

    @Resource
    private ElasticsearchRestTemplate restTemplate;

    private void printData(SearchHits<?> searchHits) {
        List<?> collect = searchHits.get().map(hit -> {
            HotelDTO hotelDTO = new HotelDTO();
            BeanUtils.copyProperties(hit.getContent(), hotelDTO);
            Map<String, List<String>> highlightFields = hit.getHighlightFields();
            if (highlightFields.size() > 0) {
                for (Map.Entry<String, List<String>> entry : highlightFields.entrySet()) {
                    BeanMap beanMap = BeanMap.create(hotelDTO);
                    beanMap.put(entry.getKey(), highlightFields.get(entry.getKey()).get(0));
                    hotelDTO = (HotelDTO) beanMap.getBean();
                }
            }
            return hotelDTO;
        }).collect(Collectors.toList());

        AtomicInteger a = new AtomicInteger(1);
        collect.forEach(h -> System.out.println(a.getAndIncrement() + "---" + JSON.toJSONString(h)));
    }

    /**
     * 搜索全部数据，按照price进行排序
     */
    @Test
    public void matchAll() {
        // 构建查询条件(搜索全部)
        MatchAllQueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        // 分页
        Pageable pageable = PageRequest.of(0, 10);
        // 排序
        FieldSortBuilder sortBuilder = new FieldSortBuilder("id").order(SortOrder.DESC);
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withPageable(pageable)
                .withSorts(sortBuilder)
                .build();
        SearchHits<HotelDoc> search = restTemplate.search(query, HotelDoc.class);
        printData(search);
    }

    /**
     * 条件搜索
     */
    @Test
    public void match() {
        // 搜索出包含 地铁 的文档
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("name", "地铁 上海").operator(Operator.AND);
        // 对于数值类型是精准匹配，对于文本类型是 模糊匹配,_score越高在前
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("brand", "速8");
        NativeSearchQuery query = new NativeSearchQueryBuilder().withQuery(matchQueryBuilder).build();
        SearchHits<HotelDoc> search = restTemplate.search(query, HotelDoc.class);
        printData(search);
    }

    /**
     * 组合搜索
     */
    @Test
    public void bool() {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // must表示同时满足，should满足其中一个，must_not表示同时不满足
        boolQueryBuilder.must(QueryBuilders.matchQuery("name", "上海"));
        boolQueryBuilder.must(QueryBuilders.termQuery("brand", "如家"));
        NativeSearchQuery query = new NativeSearchQueryBuilder().withQuery(boolQueryBuilder).build();
        SearchHits<HotelDoc> search = restTemplate.search(query, HotelDoc.class);
        printData(search);
    }

    /**
     * 过滤搜索
     */
    @Test
    public void filter() {
        // 构建条件
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("price").gte(0).lte(150);
        boolQueryBuilder.filter(rangeQueryBuilder);
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .build();
        SearchHits<HotelDoc> search = restTemplate.search(query, HotelDoc.class);
        printData(search);
    }

    /**
     * distance地理查询（圆形范围查询，指定圆心）
     */
    @Test
    public void distanceLocation() {
        GeoPoint point = new GeoPoint(31.21, 121.5);
        GeoDistanceQueryBuilder queryBuilder = QueryBuilders.geoDistanceQuery("location");
        queryBuilder.distance("2km");
        queryBuilder.point(point);
        GeoDistanceSortBuilder sortBuilder = new GeoDistanceSortBuilder("location", point).order(SortOrder.ASC);
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withSorts(sortBuilder)
                .build();
        SearchHits<HotelDoc> search = restTemplate.search(query, HotelDoc.class);
        printData(search);
    }

    /**
     * geo_bounding_box地理查询（矩形范围查询，指定左上角和右下角）
     */
    @Test
    public void boxLocation() {
        GeoPoint topLeft = new GeoPoint(31.1, 121.5);
        GeoPoint bottomRight = new GeoPoint(30.5, 121.7);
        GeoBoundingBoxQueryBuilder queryBuilder = QueryBuilders.geoBoundingBoxQuery("location").setCorners(topLeft, bottomRight);
        NativeSearchQuery query = new NativeSearchQueryBuilder().withQuery(queryBuilder).build();
        SearchHits<HotelDoc> search = restTemplate.search(query, HotelDoc.class);
        printData(search);
    }

    /**
     * 高亮查询
     */
    @Test
    public void highlight() {
        // 搜索出包含 地铁 的文档
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchQuery("name", "上海").operator(Operator.AND));
        boolQueryBuilder.must(QueryBuilders.matchQuery("brand", "如家"));
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("name");//高亮的字段
        highlightBuilder.field("brand");//高亮的字段
        highlightBuilder.requireFieldMatch(true);//是否多个字段都高亮
        highlightBuilder.preTags("<high>");
        highlightBuilder.postTags("</high>");
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withHighlightBuilder(highlightBuilder)
                .build();
        SearchHits<HotelDoc> search = restTemplate.search(query, HotelDoc.class);
        printData(search);
    }

    /**
     * 滚动查询
     * es的分页查询是将所有数据查出来然后取需要的几条，深度分页的话效率很低
     * 如果不需要翻上一页的话可以使用滚动查询
     */
    @Test
    public void scroll() {
        Criteria criteria = new Criteria();
        CriteriaQuery query = new CriteriaQuery(criteria)
                .setPageable(Pageable.ofSize(10).withPage(0))
                .addSort(Sort.by(Sort.Direction.DESC, "price"));
        query.setScrollTime(Duration.ZERO);
        SearchScrollHits<HotelDoc> scrollSearch = restTemplate.searchScrollStart(10L, query, HotelDoc.class, IndexCoordinates.of("hotel"));
        List<HotelDoc> collect = scrollSearch.get().map(SearchHit::getContent).collect(Collectors.toList());
        AtomicInteger a = new AtomicInteger(1);
        collect.forEach(h -> System.out.println(a.getAndIncrement() + "---" + JSON.toJSONString(h)));

        while (scrollSearch.hasSearchHits()) {
            scrollSearch = restTemplate.searchScrollContinue(scrollSearch.getScrollId(), 10L, HotelDoc.class, IndexCoordinates.of("hotel"));
            scrollSearch.get().map(SearchHit::getContent).collect(Collectors.toList()).forEach(h -> System.out.println(a.getAndIncrement() + "---" + JSON.toJSONString(h)));
        }
    }

    /**
     * 聚合搜索
     */
    @Test
    public void aggregations() {
        RangeQueryBuilder price = QueryBuilders.rangeQuery("price").lte(1000);
        TermsAggregationBuilder field = AggregationBuilders
                .terms("cityAgg")
                .field("city")
                .size(2)
                .order(BucketOrder.aggregation("_count",true));
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(price)
                .withPageable(Pageable.ofSize(1).withPage(0))
                .withAggregations(field)
                .build();
        SearchHits<HotelDoc> search = restTemplate.search(query, HotelDoc.class);
        //取出聚合结果
        Aggregations aggregations = (Aggregations) search.getAggregations().aggregations();
        Terms terms = (Terms) aggregations.asMap().get("cityAgg");
        for (Terms.Bucket bucket : terms.getBuckets()) {
            String keyAsString = bucket.getKeyAsString();   // 聚合字段列的值
            long docCount = bucket.getDocCount();           // 聚合字段对应的数量
            System.out.println(keyAsString + " " + docCount);
        }
    }

}
