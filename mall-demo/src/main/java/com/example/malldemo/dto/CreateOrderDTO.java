package com.example.malldemo.dto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateOrderDTO {
    @NotNull(message = "用户id不能为空")
    @Positive(message = "用户id必须大于0")
    private Long userId;

    @NotNull(message = "商品id不能为空")
    @Positive(message = "商品id必须大于0")
    private Long productId;

    @NotNull(message = "购买数量不能为空")
    @Positive(message = "购买数量必须大于0")
    private Integer quantity;
}
