package com.example.malldemo.dto;
import  lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
// MyBatis 根据订单和商品的关联查询结果组装详情响应。
@Data
public class OrderDetailVO {
    private Long id;
    private Long userId;
    private Long productId;
    private Integer quantity;
    private BigDecimal totalPrice;
    private Integer orderStatus;
    private LocalDateTime createTime;

    private ProductVO product;   // LEFT JOIN 找不到对应商品时，这里可能为 null。
}
