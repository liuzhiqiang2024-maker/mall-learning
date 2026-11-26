package com.example.malldemo.entity;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
// 对应 orders 表的一笔订单，以用户 ID 和商品 ID 关联其他表。
@Data
public class Orders {
    private Long id;
    private Long userId;
    private Long productId;
    private String productName;// ← 新增：下单时的商品名快照
    private BigDecimal unitPrice;// ← 新增：下单时的单价快照
    private Integer quantity;

    // 下单时按商品单价乘以购买数量计算并保存，之后查订单会读出这个金额。
    private BigDecimal totalPrice;
    private Integer orderStatus;
    private LocalDateTime createTime;
}
