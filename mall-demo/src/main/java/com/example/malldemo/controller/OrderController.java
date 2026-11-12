package com.example.malldemo.controller;

import com.example.malldemo.common.Result;
import com.example.malldemo.dto.OrderQueryDTO;
import com.example.malldemo.dto.OrderStatusUpdateDTO;
import com.example.malldemo.dto.OrderStatusVO;
import com.example.malldemo.dto.PageResult;
import com.example.malldemo.mapper.OrderMapper;
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
    @Autowired
    private OrderMapper orderMapper;

    @PostMapping("/create")
    public Result<Orders> createOrder(
            @RequestParam Long productId,
            @RequestParam Integer count,
            @RequestParam Long userId
    ){
        Orders order= orderService.createOrder(productId,count,userId);
        return Result.success(order);
    }
    @GetMapping("/getById")
    public Result<Orders> getOrderById(@RequestParam(required = false)
                                           @NotNull(message = "订单ID不能为空")
                                           @Positive(message = "订单ID必须大于0")
                                            Long orderId){
        Orders orders=orderService.getOrderById(orderId);
        return Result.success(orders);
    }
    @GetMapping("/page")
    public Result<PageResult<Orders>>getOrders(
            @RequestParam @NotNull(message = "页码不能为空")@Positive(message = "页码必须大于0")Integer pageNum,
            @RequestParam @NotNull(message = "每页条数不能为空")@Positive(message = "每页条数必须大于0")Integer pageSize
    ){
        //调用service
        PageResult<Orders>pageResult=orderService.getOrderPage(pageNum,pageSize);
        //包装成统一Result返回
        return Result.success(pageResult);
    }
    @PostMapping("/queryPage")
    public Result<PageResult<Orders>> queryOrders(@RequestBody
                                                      @Valid OrderQueryDTO queryDTO
    ) {
        PageResult<Orders>pageResult=orderService.queryOrderPage(queryDTO);
        return Result.success(pageResult);

    }
    @PostMapping("/updateStatus")
    public Result<OrderStatusVO> updateStatus(@RequestBody @Valid OrderStatusUpdateDTO dto){
        Orders latestOrder=orderService.updateOrderStatus(dto);
        OrderStatusVO vo=new OrderStatusVO();
        vo.setOrderId(latestOrder.getId());
        vo.setStatus(latestOrder.getOrderStatus());
        return Result.success(vo);
    }
}
