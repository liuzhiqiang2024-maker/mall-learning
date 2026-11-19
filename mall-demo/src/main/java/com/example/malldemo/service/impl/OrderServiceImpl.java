package com.example.malldemo.service.impl;
import com.example.malldemo.dto.*;
import com.example.malldemo.entity.Orders;
import com.example.malldemo.entity.Product;
import com.example.malldemo.mapper.OrderMapper;
import com.example.malldemo.mapper.ProductMapper;
import com.example.malldemo.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.malldemo.exception.BusinessException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;


@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ProductMapper productMapper;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Orders createOrder(CreateOrderDTO dto,Long currentUserId) {
        Long productId = dto.getProductId();
        Integer quantity = dto.getQuantity();

        Product product=productMapper.findById(productId);
        if(product==null){
            throw new BusinessException(400,"商品不存在");
        }
        if(!Objects.equals(product.getProductStatus(),1)){
            throw new BusinessException(400,"商品已下架，无法下单");
        }
        int rows=productMapper.deductStock(productId,quantity);
        if(rows==0){
            log.warn("库存不足，商品ID={}, 需求={}", productId, quantity);
            throw new BusinessException(400,"库存不足");
        }
        BigDecimal totalPrice=product.getPrice().multiply(BigDecimal.valueOf(quantity));
        Orders order=new Orders();
        order.setUserId(currentUserId);
        order.setProductId(productId);
        order.setQuantity(quantity);
        order.setTotalPrice(totalPrice);
        order.setOrderStatus(1); // 1待支付
        order.setCreateTime(LocalDateTime.now());
        orderMapper.insertOrder(order);
        log.info("订单创建成功，ID={}",order.getId());
        return order;
    }

    @Override
    public PageResult<Orders> getOrderPage(Integer pageNum, Integer pageSize,Long currentUserId) {
        // 1. 计算偏移量
        Integer offset = (pageNum - 1) * pageSize;
        // 2. 只查当前用户的订单
        List<Orders> records = orderMapper.selectOrderPageByUser(currentUserId,offset, pageSize);
        // 3. 查总数，总数也必须按照同一个userId统计
        Long total = orderMapper.countOrderByUser(currentUserId);
        // 4. 调用我们写好的静态方法组装分页结果,组装
        return PageResult.build(records, total,pageNum, pageSize);
    }
    @Override
    public PageResult<Orders>queryOrderPage(OrderQueryDTO queryDTO,Long currentUserId) {
        return getOrderPage(queryDTO.getPageNum(), queryDTO.getPageSize(), currentUserId);
    }
    @Override
    public Orders updateOrderStatus(OrderStatusUpdateDTO dto,Long currentUserId) {
        //1.查询订单
        Orders order = orderMapper.selectOrderById(dto.getOrderId());
        //2.判断订单是否存在
        if (order == null) {
            throw new BusinessException(404, "订单不存在");
        }
        //3.不是自己的订单也返回404
        if(!Objects.equals(order.getUserId(),currentUserId)){
            log.warn("越权修改订单状态：orderId={},订单归属UserId={},当前userId={},目标状态={}",
                    dto.getOrderId(),order.getUserId(),currentUserId,dto.getStatus());
            throw new BusinessException(404,"订单不存在");
        }
        Integer oldStatus = order.getOrderStatus();
        Integer newStatus = dto.getStatus();
        //3.判断新状态是否合法，只能0.1，2，3，4
        if (!Set.of(0,1, 2, 3, 4).contains(newStatus)) {
            throw new BusinessException(400, "订单状态参数非法");
        }
        //4.状态流转校验
        boolean allow = false;
        if (oldStatus == 0) {
            if(newStatus ==1){
                allow = true;
            }
        }else if (oldStatus == 1) {
            //待付款；可以转2，3
            if (newStatus == 2 || newStatus == 3) {
                allow = true;
            }
        } else if (oldStatus == 2) {
            //已支付：只能转4
            if (newStatus == 4) {
                allow = true;
            }
        }
        //oldStatus=3、4 是终态，allow保持false
        if (!allow) {
            throw new BusinessException(400, "订单状态不允许变更");
        }
        // 5. 执行更新
        int rows = orderMapper.updateOrderStatus(dto.getOrderId(), newStatus);
        if (rows != 1) {
            throw new BusinessException(400,"订单状态更新失败");
        }
        // 6. 更新成功，重新查询最新订单返回
        return orderMapper.selectOrderById(dto.getOrderId());
    }
    @Override
    public OrderDetailVO getOrderById(Long orderId,Long currentUserId) {
        OrderDetailVO detail=orderMapper.selectOrderDetailById(orderId);
        //1.订单真不存在，404
        if(detail==null){
            throw new BusinessException(404,"订单不存在");
        }
        //2.订单存在，但不是当前用户，也返回404
        if(!Objects.equals(detail.getUserId(),currentUserId)){
            log.warn("越权访问订单：orderId={},订单归属userId={}，当前userId={}",orderId,detail.getUserId(),currentUserId);
            throw new BusinessException(404,"订单不存在");
        }
        return detail;
    }

}