package com.macd.ps.model;

import com.macd.ps.entity.Card;
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
public class PaymentDto {
    private long id;
    private long orderNumber;
    private LocalDateTime paymentDate;
    private double totalAmount;
    private Card card;
}
