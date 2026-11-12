package com.example.malldemo.service.impl;
import com.example.malldemo.dto.OrderQueryDTO;
import com.example.malldemo.dto.OrderStatusUpdateDTO;
import com.example.malldemo.dto.PageResult;
import com.example.malldemo.entity.Orders;
import com.example.malldemo.entity.Product;
import com.example.malldemo.mapper.OrderMapper;
import com.example.malldemo.mapper.ProductMapper;
import com.example.malldemo.service.OrderService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.malldemo.exception.BusinessException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ProductMapper productMapper;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Orders createOrder(Long productId, Integer count, Long userId){
        Product product=productMapper.findById(productId);
        if(product==null){
            throw new BusinessException(400,"商品不存在");
        }
        int rows=productMapper.deductStock(productId,count);
        if(rows==0){
            log.warn("库存不足，商品ID={}, 需求={}", productId, count);
            throw new BusinessException(400,"库存不足");
        }
        BigDecimal totalPrice=product.getPrice().multiply(BigDecimal.valueOf(count));
        Orders order=new Orders();
        order.setUserId(userId);
        order.setProductId(productId);
        order.setQuantity(count);
        order.setTotalPrice(totalPrice);
        order.setOrderStatus(0); // 0代表未支付
        order.setCreateTime(LocalDateTime.now());
        orderMapper.insertOrder(order);
        log.info("订单创建成功，ID={}",order.getId());
        return order;
    }
    @Override
    public Orders getOrderById(Long orderId) {
        // 1.调用mapper查询
        Orders orders=orderMapper.selectOrderById(orderId);
        // 2.业务判断，如果查询结果为null，抛自定义业务异常
        if(orders==null){
            throw new BusinessException(500,"订单不存在");
        }
        // 3.查到直接返回订单对象
        return orders;

    }
    @Override
    public PageResult<Orders> getOrderPage(Integer pageNum, Integer pageSize) {
        // 1. 计算偏移量
        Integer offset = (pageNum - 1) * pageSize;
        // 2. 查询当前页数据
        List<Orders> records = orderMapper.selectOrderPage(offset, pageSize);
        // 3. 查询总条数
        Long total = orderMapper.selectOrderCount();
        // 4. 调用我们写好的静态方法组装分页结果
        return PageResult.build(records, total,pageNum, pageSize);
    }
    @Override
    public PageResult<Orders>queryOrderPage(OrderQueryDTO queryDTO) {
        //1.查询总数
        Long total =orderMapper.countOrderPage(queryDTO);
        //没有数据直接返回空
        if(total==0){
            return PageResult.build(Collections.emptyList(),total,queryDTO.getPageNum(),queryDTO.getPageSize());
        }
        // 2. 计算offset
        Integer offset = (queryDTO.getPageNum() - 1) * queryDTO.getPageSize();
        // 3. 查询订单列表
        List<Orders> orderList = orderMapper.selectOrderPage(queryDTO, offset, queryDTO.getPageSize());
        // 4. 组装分页对象返回
        return PageResult.build(orderList, total, queryDTO.getPageNum(), queryDTO.getPageSize());
    }
    @Override
    public Orders updateOrderStatus(OrderStatusUpdateDTO dto) {
        //1.查询订单
        Orders order = orderMapper.selectOrderById(dto.getOrderId());
        //2.判断订单是否存在
        if (order == null) {
            throw new BusinessException(400, "订单不存在");
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

}