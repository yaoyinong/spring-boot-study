package com.elasticsearch.test;

import com.alibaba.fastjson.JSON;
import com.elasticsearch.dto.HotelDTO;
import com.elasticsearch.es.doc.HotelDoc;
import com.elasticsearch.es.repository.HotelEsRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.util.QueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.highlight.Highlight;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightField;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightFieldParameters;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightParameters;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    private HotelEsRepository hotelEsRepository;

    @Resource
    private ElasticsearchRestTemplate restTemplate;

    /**
     * 查询所有
     */
    @Test
    public void matchAll() {
        Iterable<HotelDoc> all = hotelEsRepository.findAll(Sort.by(Sort.Direction.DESC, "price"));
        AtomicInteger a = new AtomicInteger(1);
        all.forEach(h -> {
            System.out.println(a.getAndIncrement() + "---" + JSON.toJSONString(h));
        });
    }

    /**
     * 分页查询
     */
    @Test
    public void page() {
        Iterable<HotelDoc> all = hotelEsRepository.findAll(Pageable.ofSize(10).withPage(2));
        AtomicInteger a = new AtomicInteger(1);
        all.forEach(h -> {
            System.out.println(a.getAndIncrement() + "---" + JSON.toJSONString(h));
        });
    }

    /**
     * 多条件（模糊）分页查询
     */
    @Test
    public void matchPage() {
        Criteria criteria = new Criteria();
        criteria.and(new Criteria("name").matches("希尔顿"));
        criteria.and(new Criteria("city").in("北京","上海"));
        criteria.and(new Criteria("price").greaterThanEqual("1000").lessThanEqual("2000"));
        CriteriaQuery query = new CriteriaQuery(criteria)
                .setPageable(Pageable.ofSize(10).withPage(0))
                .addSort(Sort.by(Sort.Direction.DESC, "price"));
        SearchHits<HotelDoc> search = restTemplate.search(query, HotelDoc.class);
        List<HotelDoc> collect = search.get().map(SearchHit::getContent).collect(Collectors.toList());
        AtomicInteger a = new AtomicInteger(1);
        collect.forEach(h -> System.out.println(a.getAndIncrement() + "---" + JSON.toJSONString(h)));
    }

    /**
     * range范围查询
     * between
     */
    @Test
    public void range() {
        Criteria criteria = new Criteria("price").between(0,150);
        CriteriaQuery query = new CriteriaQuery(criteria)
                .setPageable(Pageable.ofSize(10).withPage(0))
                .addSort(Sort.by(Sort.Direction.DESC, "price"));
        SearchHits<HotelDoc> search = restTemplate.search(query, HotelDoc.class);
        List<HotelDoc> collect = search.get().map(SearchHit::getContent).collect(Collectors.toList());
        AtomicInteger a = new AtomicInteger(1);
        collect.forEach(h -> System.out.println(a.getAndIncrement() + "---" + JSON.toJSONString(h)));
    }

    /**
     * distance地理查询（圆形范围查询，指定圆心）
     */
    @Test
    public void distanceLocation() {
        GeoPoint point = new GeoPoint(31.21, 121.5);
        Criteria criteria = new Criteria("location").within(point, "2km");
        CriteriaQuery query = new CriteriaQuery(criteria);
        SearchHits<HotelDoc> search = restTemplate.search(query, HotelDoc.class);
        List<HotelDoc> collect = search.get().map(SearchHit::getContent).collect(Collectors.toList());
        AtomicInteger a = new AtomicInteger(1);
        collect.forEach(h -> System.out.println(a.getAndIncrement() + "---" + JSON.toJSONString(h)));
    }

    /**
     * geo_bounding_box地理查询（矩形范围查询，指定左上角和右下角）
     */
    @Test
    public void boxLocation() {
        GeoPoint topLeft = new GeoPoint(31.1,121.5);
        GeoPoint bottomRight = new GeoPoint(30.5,121.7);
        Criteria criteria = new Criteria("location").boundedBy(topLeft,bottomRight);
        CriteriaQuery query = new CriteriaQuery(criteria);
        SearchHits<HotelDoc> search = restTemplate.search(query, HotelDoc.class);
        List<HotelDoc> collect = search.get().map(SearchHit::getContent).collect(Collectors.toList());
        AtomicInteger a = new AtomicInteger(1);
        collect.forEach(h -> System.out.println(a.getAndIncrement() + "---" + JSON.toJSONString(h)));
    }

    /**
     * 高亮查询
     */
    @Test
    public void highlight() {
        Criteria criteria = new Criteria("all").matches("如家");
        CriteriaQuery query = new CriteriaQuery(criteria);

        // 高亮
        HighlightParameters build = HighlightParameters.builder()
                .withRequireFieldMatch(false)
                .withPreTags(new String[]{"<high>"})
                .withPostTags(new String[]{"</high>"})
                .build();
        Highlight highlight = new Highlight(build, Collections.singletonList(new HighlightField("name")));
        HighlightQuery highlightQuery = new HighlightQuery(highlight,HotelDoc.class);
        query.setHighlightQuery(highlightQuery);
        SearchHits<HotelDoc> search = restTemplate.search(query, HotelDoc.class);
        List<HotelDTO> dtoList = search.get().map(s -> {
            HotelDTO dto = new HotelDTO();
            BeanUtils.copyProperties(s.getContent(), dto);
            dto.setHighlight(s.getHighlightFields());
            return dto;
        }).collect(Collectors.toList());
        AtomicInteger a = new AtomicInteger(1);
        dtoList.forEach(dto -> {
            System.out.println(a.getAndIncrement() + ">>>highlight>>" + JSON.toJSONString(dto));
        });
    }

}
