package com.macd.ps.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class PaymentDto {
    private long id;
    private long orderNumber;
    private LocalDateTime paymentDate;
    private BigDecimal totalAmount;
}
