package com.example.malldemo.dto;
import  lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
public class OrderDetailVO {
    private Long id;
    private Long userId;
    private Long productId;
    private Integer quantity;
    private BigDecimal totalPrice;
    private Integer orderStatus;
    private LocalDateTime createTime;

    private ProductVO product;   // 关联出来的商品信息
}
