package com.elasticsearch.test;

import com.alibaba.fastjson.JSON;
import com.elasticsearch.entity.TbHotel;
import com.elasticsearch.es.repository.HotelEsRepository;
import com.elasticsearch.es.doc.HotelDoc;
import com.elasticsearch.service.ITbHotelService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
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
        List<HotelDoc> collect = list.stream().map(h -> {
            HotelDoc model = new HotelDoc();
            BeanUtils.copyProperties(h, model);
            model.setLocation(new GeoPoint(Double.parseDouble(h.getLatitude()), Double.parseDouble(h.getLongitude())));
            model.setIsAd(false);
            return model;
        }).collect(Collectors.toList());
        hotelEsRepository.saveAll(collect);
        log.info("插入数据:{}条", list.size());
    }

    /**
     * 根据ID修改文档
     */
    @Test
    public void updateDocumentById() {
        Optional<HotelDoc> byId = hotelEsRepository.findById(2051661320L);
        if (byId.isPresent()) {
            HotelDoc hotelIndexMapping = byId.get();
            hotelIndexMapping.setStarName("四钻");
            hotelEsRepository.save(hotelIndexMapping);
        }
    }

    /**
     * 根据ID查询文档
     */
    @Test
    public void selectDocumentById() {
        Optional<HotelDoc> byId = hotelEsRepository.findById(2048050570L);
        if (byId.isPresent()) {
            HotelDoc hotel = byId.get();
            System.out.println(JSON.toJSONString(hotel));
        } else {
            log.info("根据ID查询文档失败，无数据,ID:{}", 2048050570L);
        }
    }

    /**
     * 根据ID删除文档
     */
    @Test
    public void deleteDocumentById() {
        hotelEsRepository.deleteById(2051661320L);
    }



}
