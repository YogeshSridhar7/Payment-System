package com.macd.ps.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class CardDto {
    private long id;
    private String cardHolderName;
    private long cardNumber;
    private String expiryDate;
}
