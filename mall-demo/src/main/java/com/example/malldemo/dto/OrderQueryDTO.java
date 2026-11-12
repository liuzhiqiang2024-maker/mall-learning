package com.example.malldemo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class OrderQueryDTO {
    @NotNull
    @Positive
    private Integer pageNum;

    @NotNull
    @Positive
    private Integer pageSize;

    @NotNull
    @Positive
    private Long userId;
}
