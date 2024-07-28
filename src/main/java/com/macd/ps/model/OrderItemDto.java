package com.macd.ps.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class OrderItemDto {
    private Long itemId;
    private String itemName;
    private Float itemCost;
    private Long itemQuantity;
}
