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
    // 扣库存和写入订单共用事务。方法抛出异常时，Spring 会回滚这两次写入。
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Orders createOrder(CreateOrderDTO dto,Long currentUserId) {
        Long productId = dto.getProductId();
        Integer quantity = dto.getQuantity();

        Product product=productMapper.findById(productId);
        if(product==null){
            throw new BusinessException(400,"商品不存在");
        }
        // 商品状态 1 表示上架。这里使用查到的商品状态判断能否下单。
        if(!Objects.equals(product.getProductStatus(),1)){
            throw new BusinessException(400,"商品已下架，无法下单");
        }
        // Mapper 在一条 SQL 中检查并扣减库存，返回这次更新影响的行数。
        int rows=productMapper.deductStock(productId,quantity);
        if(rows==0){
            log.warn("库存不足，商品ID={}, 需求={}", productId, quantity);
            throw new BusinessException(400,"库存不足");
        }
        // 用数据库查出的单价乘以购买数量，BigDecimal 用于十进制金额计算。
        BigDecimal totalPrice=product.getPrice().multiply(BigDecimal.valueOf(quantity));
        Orders order=new Orders();
        order.setUserId(currentUserId);
        order.setProductId(productId);
        order.setProductName(product.getProductName());
        order.setQuantity(quantity);
        order.setUnitPrice(product.getPrice());
        order.setTotalPrice(totalPrice);
        order.setOrderStatus(1); // 新订单从待支付（1）开始
        order.setCreateTime(LocalDateTime.now());
        // 插入成功后，MyBatis 将数据库生成的主键回填到 order.id。
        orderMapper.insertOrder(order);
        log.info("订单创建成功，ID={}",order.getId());
        return order;
    }

    @Override
    public PageResult<Orders> getOrderPage(Integer pageNum, Integer pageSize,Long currentUserId) {
        // 页码从 1 开始。第 2 页每页 10 条时，跳过前 10 条。
        Integer offset = (pageNum - 1) * pageSize;
        // 分页记录和总数分开查询，两次都用当前用户 ID 筛选。
        List<Orders> records = orderMapper.selectOrderPageByUser(currentUserId,offset, pageSize);
        Long total = orderMapper.countOrderByUser(currentUserId);
        return PageResult.build(records, total,pageNum, pageSize);
    }
    @Override
    public PageResult<Orders>queryOrderPage(OrderQueryDTO queryDTO,Long currentUserId) {
        // 两种分页接口共用同一套查询逻辑。
        return getOrderPage(queryDTO.getPageNum(), queryDTO.getPageSize(), currentUserId);
    }
    @Override
    public Orders updateOrderStatus(OrderStatusUpdateDTO dto,Long currentUserId) {
        Orders order = orderMapper.selectOrderById(dto.getOrderId());
        // 查不到订单或订单归属其他用户时，统一返回业务码 404，避免透露订单是否存在。
        if (order == null) {
            throw new BusinessException(404, "订单不存在");
        }
        if(!Objects.equals(order.getUserId(),currentUserId)){
            log.warn("越权修改订单状态：orderId={},订单归属UserId={},当前userId={},目标状态={}",
                    dto.getOrderId(),order.getUserId(),currentUserId,dto.getStatus());
            throw new BusinessException(404,"订单不存在");
        }
        Integer oldStatus = order.getOrderStatus();
        Integer newStatus = dto.getStatus();
        // 状态值范围为 0 到 4。入口 DTO 要求目标状态为正数，0 可作为已有订单的旧状态。
        if (!Set.of(0,1, 2, 3, 4).contains(newStatus)) {
            throw new BusinessException(400, "订单状态参数非法");
        }
        // 根据旧状态判断本次流转是否允许。本方法的业务动作限于更新订单状态字段。
        boolean allow = false;
        if (oldStatus == 0) {
            if(newStatus ==1){
                allow = true;
            }
        }else if (oldStatus == 1) {
            // 待支付（1）允许转到 2 或 3。
            if (newStatus == 2 || newStatus == 3) {
                allow = true;
            }
        } else if (oldStatus == 2) {
            // 已支付（2）只能转到 4。
            if (newStatus == 4) {
                allow = true;
            }
        }
        // 状态 3 和 4 为终态，allow 保持为 false。
        if (!allow) {
            throw new BusinessException(400, "订单状态不允许变更");
        }
        // SQL 只按订单 ID 更新，并发请求可能基于同一个旧状态通过校验。
        int rows = orderMapper.updateOrderStatus(dto.getOrderId(), newStatus);
        if (rows != 1) {
            throw new BusinessException(400,"订单状态更新失败");
        }
        // 更新后重新读取订单，调用方从结果中取出订单 ID 和状态。
        return orderMapper.selectOrderById(dto.getOrderId());
    }
    @Override
    public OrderDetailVO getOrderById(Long orderId,Long currentUserId) {
        OrderDetailVO detail=orderMapper.selectOrderDetailById(orderId);
        // 未查到详情和详情归属其他用户时共用业务码 404。
        if(detail==null){
            throw new BusinessException(404,"订单不存在");
        }
        if(!Objects.equals(detail.getUserId(),currentUserId)){
            log.warn("越权访问订单：orderId={},订单归属userId={}，当前userId={}",orderId,detail.getUserId(),currentUserId);
            throw new BusinessException(404,"订单不存在");
        }
        return detail;
    }




}