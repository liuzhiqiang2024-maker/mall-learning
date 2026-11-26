package com.example.malldemo.dto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
// 这里接收目标状态，Service 根据数据库里的当前状态判断是否允许变更。
@Data
public class OrderStatusUpdateDTO {
    @NotNull(message = "订单id不能为空")
    @Positive(message = "订单id必须是正数")
    private Long orderId;

    @NotNull(message = "订单状态不能为空")
    @Positive(message = "订单状态必须是正数")
    private Integer status;
}
