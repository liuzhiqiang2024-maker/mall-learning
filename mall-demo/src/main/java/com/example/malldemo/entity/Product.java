package com.example.malldemo.entity;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
// 对应 product 表。下单时 Service 读取单价和上架状态，库存扣减由 Mapper 的 SQL 完成。
@Data
public class Product {
    private Long id;
    private String  productName;
    private BigDecimal price;
    private Integer stock;
    private Integer productStatus;
    private LocalDateTime createTime;
}
