package com.example.malldemo.service;
import com.example.malldemo.dto.*;
import com.example.malldemo.entity.Orders;
import jakarta.validation.Valid;



public interface OrderService {


    PageResult<Orders> getOrderPage(Integer pageNum, Integer pageSize,Long currentUserId);
    PageResult<Orders>queryOrderPage(OrderQueryDTO orderQueryDTO,Long currentUserId);
    OrderDetailVO getOrderById(Long orderId,Long currentUserId);
    Orders updateOrderStatus(@Valid OrderStatusUpdateDTO dto,Long currentUserId);
    Orders createOrder(CreateOrderDTO dto,Long currentUserId);

}
