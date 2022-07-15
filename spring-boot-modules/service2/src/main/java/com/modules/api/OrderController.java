package com.modules.api;

import com.modules.entity.OrderInfo;
import com.modules.service.OrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yaoyinong
 * @date 2022/7/15 08:51
 * @description Order测试API
 */
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderInfoService orderInfoService;

    @GetMapping("getOrderByOrderId/{orderId}")
    public OrderInfo getOrderById(@PathVariable String orderId) {
        return orderInfoService.getByOrderId(orderId);
    }

}
