package com.example.malldemo.dto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
@Data
public class OrderStatusUpdateDTO {
    @NotNull(message = "订单id不能为空")
    @Positive(message = "订单id必须是正数")
    private Long orderId;

    @NotNull(message = "订单状态不能为空")
    @Positive(message = "订单状态必须是正数")
    private Integer status;
}
