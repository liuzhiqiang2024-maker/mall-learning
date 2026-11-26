package com.example.malldemo.dto;
import lombok.Data;
import java.math.BigDecimal;
// 订单详情中嵌套的商品信息，字段来自关联查询时的 product 表。
@Data
public class ProductVO {
    private Long id;
    private String productName;
    // 此处是查询时的商品单价，订单总价来自创建时写入的 totalPrice。
    private BigDecimal price;
}
