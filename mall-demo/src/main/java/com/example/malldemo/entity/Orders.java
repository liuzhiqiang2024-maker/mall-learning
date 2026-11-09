package com.example.malldemo.entity;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
public class Orders {
    private Long id;
    private Long userId;
    private Long productId;
    private Integer quantity;
    private BigDecimal totalPrice;
    private Integer orderStatus;
    private LocalDateTime createTime;
}
