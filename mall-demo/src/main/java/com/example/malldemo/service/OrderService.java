package com.example.malldemo.service;
import com.example.malldemo.dto.OrderQueryDTO;
import com.example.malldemo.dto.PageResult;
import com.example.malldemo.entity.Orders;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface OrderService {
    Orders createOrder(Long productId,Integer count,Long userId);
    Orders getOrderById(Long orderId);
    PageResult<Orders> getOrderPage(Integer pageNum, Integer pageSize);
    PageResult<Orders>queryOrderPage(OrderQueryDTO orderQueryDTO);
}
