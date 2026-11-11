package com.example.malldemo.service.impl;
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
import java.util.List;

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
        return PageResult.build(records, total, pageSize);
    }



}