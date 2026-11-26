package com.example.malldemo.dto;
import lombok.Data;
// 状态更新接口把更新后重新查到的订单 ID 和状态放进响应。
@Data
public class OrderStatusVO {
    private Long orderId;
    private Integer Status;
}
