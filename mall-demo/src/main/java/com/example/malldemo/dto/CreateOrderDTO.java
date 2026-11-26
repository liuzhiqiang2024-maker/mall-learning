package com.example.malldemo.dto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

// 客户端提交商品 ID 和购买数量。服务端从登录身份取得用户 ID，再按数据库单价计算总价。
@Data
public class CreateOrderDTO {


    @NotNull(message = "商品id不能为空")
    @Positive(message = "商品id必须大于0")
    private Long productId;

    @NotNull(message = "购买数量不能为空")
    @Positive(message = "购买数量必须大于0")
    private Integer quantity;
}
