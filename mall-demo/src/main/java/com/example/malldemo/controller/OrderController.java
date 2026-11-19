package com.example.malldemo.controller;

import com.example.malldemo.auth.AuthInterceptor;
import com.example.malldemo.common.Result;
import com.example.malldemo.dto.*;
import com.example.malldemo.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.example.malldemo.entity.Orders;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
@RestController
@RequestMapping("/order")
@Validated
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public Result<Orders> createOrder(
            @RequestAttribute(AuthInterceptor.ATTR_USER_ID) Long currentUserId,
            @RequestBody @Valid CreateOrderDTO dto)
    {
        Orders order= orderService.createOrder(dto,currentUserId);
        return Result.success(order);
    }
    @GetMapping("/getById")
    public Result<OrderDetailVO> getOrderById(
                                            @RequestAttribute(AuthInterceptor.ATTR_USER_ID) Long currentUserId,
                                            @RequestParam(required = false)
                                            @NotNull(message = "订单ID不能为空")
                                            @Positive(message = "订单ID必须大于0")
                                            Long orderId){
        OrderDetailVO detail =orderService.getOrderById(orderId,currentUserId);
        return Result.success(detail);
    }
    @GetMapping("/page")
    public Result<PageResult<Orders>>getOrders(
            @RequestAttribute(AuthInterceptor.ATTR_USER_ID) Long currentUserId,
            @RequestParam @NotNull(message = "页码不能为空")@Positive(message = "页码必须大于0")Integer pageNum,
            @RequestParam @NotNull(message = "每页条数不能为空")@Positive(message = "每页条数必须大于0")Integer pageSize

    ){
        //调用service
        PageResult<Orders>pageResult=orderService.getOrderPage(pageNum,pageSize,currentUserId);
        //包装成统一Result返回
        return Result.success(pageResult);
    }
    @PostMapping("/queryPage")
    public Result<PageResult<Orders>> queryOrders(
            @RequestBody @Valid OrderQueryDTO queryDTO,
            @RequestAttribute(AuthInterceptor.ATTR_USER_ID)Long currentUserId
    ) {
        PageResult<Orders>pageResult=orderService.queryOrderPage(queryDTO,currentUserId);
        return Result.success(pageResult);

    }
    @PostMapping("/updateStatus")
    public Result<OrderStatusVO> updateStatus(
            @RequestAttribute(AuthInterceptor.ATTR_USER_ID) Long currentUserId,
            @RequestBody @Valid OrderStatusUpdateDTO dto){
        Orders latestOrder=orderService.updateOrderStatus(dto,currentUserId);
        OrderStatusVO vo=new OrderStatusVO();
        vo.setOrderId(latestOrder.getId());
        vo.setStatus(latestOrder.getOrderStatus());
        return Result.success(vo);
    }
}
