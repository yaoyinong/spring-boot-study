package com.modules.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.modules.entity.OrderInfo;
import com.modules.mapper.OrderInfoMapper;
import com.modules.service.OrderInfoService;
import org.springframework.stereotype.Service;

/**
 * @author yaoyinong
 * @date 2022/7/14 22:13
 * @description OrderInfoServiceImpl
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {

    @Override
    public OrderInfo getByOrderId(String orderId) {
        return getOne(new LambdaQueryWrapper<OrderInfo>().eq(OrderInfo::getOrderId, orderId).last("limit 10"));
    }

}
