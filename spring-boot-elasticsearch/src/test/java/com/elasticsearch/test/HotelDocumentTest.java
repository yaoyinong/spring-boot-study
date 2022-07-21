package com.elasticsearch.test;

import com.elasticsearch.entity.TbHotel;
import com.elasticsearch.es.repository.HotelEsRepository;
import com.elasticsearch.es.mapping.GeoPointMapping;
import com.elasticsearch.es.mapping.HotelIndexMapping;
import com.elasticsearch.service.ITbHotelService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author yaoyinong
 * @date 2022/7/21 23:54
 * @description 文档测试
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class HotelDocumentTest {

    @Resource
    private ITbHotelService hotelService;

    @Resource
    private HotelEsRepository hotelEsRepository;

    /**
     * 保存数据到ES
     */
    @Test
    public void saveDocument() {
        List<TbHotel> list = hotelService.list();
        log.info("插入数据:{}条", list.size());

        List<HotelIndexMapping> collect = list.stream().map(h -> {
            HotelIndexMapping model = new HotelIndexMapping();
            BeanUtils.copyProperties(h, model);
            model.setGeoPoint(new GeoPointMapping(h.getLatitude(), h.getLongitude()));
            return model;
        }).collect(Collectors.toList());
        hotelEsRepository.saveAll(collect);
    }

    /**
     * 根据ID查询文档
     */
    @Test
    public void selectDocumentById() {
        Optional<HotelIndexMapping> byId = hotelEsRepository.findById(2051661320L);
        if (byId.isPresent()) {
            HotelIndexMapping hotelIndexMappingModel = byId.get();
            log.info("根据ID查询文档成功：{}", hotelIndexMappingModel);
        } else {
            log.info("根据ID查询文档失败，无数据,ID:{}", 2051661320L);
        }
    }

    /**
     * 根据ID删除文档
     */
    @Test
    public void deleteDocumentById() {
        hotelEsRepository.deleteById(2048671293L);
    }

    /**
     * 根据ID修改文档
     */
    @Test
    public void updateDocumentById() {
        Optional<HotelIndexMapping> byId = hotelEsRepository.findById(2051661320L);
        if (byId.isPresent()) {
            HotelIndexMapping hotelIndexMapping = byId.get();
            hotelIndexMapping.setStarName("四钻");
            hotelEsRepository.save(hotelIndexMapping);
        }


    }

}
