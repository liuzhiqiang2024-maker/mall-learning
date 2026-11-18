package com.example.malldemo.service;
import com.example.malldemo.dto.*;
import com.example.malldemo.entity.Orders;
import jakarta.validation.Valid;



public interface OrderService {
    Orders createOrder(CreateOrderDTO dto);

    PageResult<Orders> getOrderPage(Integer pageNum, Integer pageSize);
    PageResult<Orders>queryOrderPage(OrderQueryDTO orderQueryDTO);
    OrderDetailVO getOrderById(Long orderId);
    Orders updateOrderStatus(@Valid OrderStatusUpdateDTO dto);
}
