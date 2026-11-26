package com.example.malldemo.service;
import com.example.malldemo.dto.*;
import com.example.malldemo.entity.Orders;
import jakarta.validation.Valid;



/** Controller 通过这个接口调用订单业务，具体规则在 OrderServiceImpl 中。 */
public interface OrderService {


    PageResult<Orders> getOrderPage(Integer pageNum, Integer pageSize,Long currentUserId);
    PageResult<Orders>queryOrderPage(OrderQueryDTO orderQueryDTO,Long currentUserId);
    OrderDetailVO getOrderById(Long orderId,Long currentUserId);
    Orders updateOrderStatus(@Valid OrderStatusUpdateDTO dto,Long currentUserId);
    // dto 提供下单参数，currentUserId 是服务端确认的下单人。
    Orders createOrder(CreateOrderDTO dto,Long currentUserId);

}
