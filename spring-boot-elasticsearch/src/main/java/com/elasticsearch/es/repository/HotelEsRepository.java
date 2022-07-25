package com.elasticsearch.es.repository;

import com.elasticsearch.es.doc.HotelDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author yaoyinong
 * @date 2022/7/22 00:58
 * @description es数据操作接口
 */
public interface HotelEsRepository extends ElasticsearchRepository<HotelDoc, Long> {
}
