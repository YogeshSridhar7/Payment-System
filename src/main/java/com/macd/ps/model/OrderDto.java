package com.macd.ps.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class OrderDto {
    private int id;
    private LocalDateTime orderDate;
    private double totalAmount;
    private List<OrderItemDto> orderItem;
    private PaymentDto paymentDto;

}
