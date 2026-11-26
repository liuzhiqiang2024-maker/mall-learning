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
// @RequestAttribute 读取登录拦截器写入的用户 ID。客户端的请求参数由各接口单独接收。
@RestController
@RequestMapping("/order")
@Validated
public class OrderController {
    @Autowired
    private OrderService orderService;

    // 请求体只接收商品 ID 和购买数量，@Valid 会在调用方法前检查这两个字段。
    @PostMapping("/create")
    public Result<Orders> createOrder(
            @RequestAttribute(AuthInterceptor.ATTR_USER_ID) Long currentUserId,
            @RequestBody @Valid CreateOrderDTO dto)
    {
        Orders order= orderService.createOrder(dto,currentUserId);
        return Result.success(order);
    }
    // orderId 来自 URL 查询参数，订单归属由 Service 检查。
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
    // GET 分页从 URL 查询参数接收页码和每页条数。
    @GetMapping("/page")
    public Result<PageResult<Orders>>getOrders(
            @RequestAttribute(AuthInterceptor.ATTR_USER_ID) Long currentUserId,
            @RequestParam @NotNull(message = "页码不能为空")@Positive(message = "页码必须大于0")Integer pageNum,
            @RequestParam @NotNull(message = "每页条数不能为空")@Positive(message = "每页条数必须大于0")Integer pageSize

    ){
        PageResult<Orders>pageResult=orderService.getOrderPage(pageNum,pageSize,currentUserId);
        return Result.success(pageResult);
    }
    // POST 分页从 JSON 请求体接收同样的分页参数。
    @PostMapping("/queryPage")
    public Result<PageResult<Orders>> queryOrders(
            @RequestBody @Valid OrderQueryDTO queryDTO,
            @RequestAttribute(AuthInterceptor.ATTR_USER_ID)Long currentUserId
    ) {
        PageResult<Orders>pageResult=orderService.queryOrderPage(queryDTO,currentUserId);
        return Result.success(pageResult);

    }
    // 请求体提供订单 ID 和目标状态，响应给出订单 ID 和更新后的状态。
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
