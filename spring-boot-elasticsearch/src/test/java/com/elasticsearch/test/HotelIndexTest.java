package com.elasticsearch.test;

import com.elasticsearch.es.doc.HotelDoc;
import com.elasticsearch.model.dto.HotelDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author yaoyinong
 * @date 2022/7/21 21:04
 * @description 索引库测试
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class HotelIndexTest {

    @Resource
    private ElasticsearchRestTemplate restTemplate;

    /**
     * 验证索引库是否存在
     * @param clazz 索引库映射对象
     * @return true/false
     */
    private Boolean indexIsExists(Class<?> clazz) {
        return restTemplate.indexOps(clazz).exists();
    }

    /**
     * 创建索引库和映射
     * 相当于MySQL建表建字段
     */
    @Test
    public void createIndexAndMapping() {
        boolean withMapping = restTemplate.indexOps(HotelDoc.class).createWithMapping();
        log.info("创建索引库：{}", withMapping);
        log.info("验证索引库是否存在：{}", indexIsExists(HotelDoc.class));
    }

    /**
     * 删除索引库
     * 相当于MySQL删除表
     */
    @Test
    public void deleteIndex() {
        Boolean isExists = indexIsExists(HotelDoc.class);
        log.info("索引库是否存在：{}", isExists);
        boolean delete = false;
        if (isExists) {
            delete = restTemplate.indexOps(HotelDoc.class).delete();
        }
        log.info("删除索引库：{}", delete);
    }



}
