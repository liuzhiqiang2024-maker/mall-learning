package com.example.malldemo.entity;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
public class Product {
    private Long id;
    private String  productName;
    private BigDecimal price;
    private Integer stock;
    private Integer productStatus;
    private LocalDateTime createTime;
}
