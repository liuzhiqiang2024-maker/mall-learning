package com.example.malldemo.dto;
import lombok.Data;
import java.math.BigDecimal;
@Data
public class ProductVO {
    private Long id;
    private String productName;
    private BigDecimal price;
}
