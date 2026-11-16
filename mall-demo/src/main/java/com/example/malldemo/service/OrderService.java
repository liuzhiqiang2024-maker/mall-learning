package com.example.malldemo.service;
import com.example.malldemo.dto.OrderQueryDTO;
import com.example.malldemo.dto.OrderStatusUpdateDTO;
import com.example.malldemo.dto.PageResult;
import com.example.malldemo.entity.Orders;
import jakarta.validation.Valid;
import com.example.malldemo.dto.CreateOrderDTO;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface OrderService {
    Orders createOrder(CreateOrderDTO dto);
    Orders getOrderById(Long orderId);
    PageResult<Orders> getOrderPage(Integer pageNum, Integer pageSize);
    PageResult<Orders>queryOrderPage(OrderQueryDTO orderQueryDTO);

    Orders updateOrderStatus(@Valid OrderStatusUpdateDTO dto);
}
