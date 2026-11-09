package com.example.malldemo.controller;

import com.example.malldemo.common.Result;
import com.example.malldemo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.malldemo.entity.Orders;
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
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
    public Result<Orders> getOrderById(@RequestParam Long orderId){
        Orders orders=orderService.getOrderById(orderId);
        return Result.success(orders);
    }
}
