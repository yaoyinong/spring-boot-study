package com.modules.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.modules.entity.OrderInfo;

/**
 * @author yaoyinong
 * @date 2022/7/14 22:07
 * @description UserInfoService
 */
public interface OrderInfoService extends IService<OrderInfo> {

    OrderInfo getByOrderId(String orderId);

}
