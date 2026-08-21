package com.macd.ps.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ModelTest {

    @Test
    void buildsOrderWithPayment() {
        PaymentDto payment = PaymentDto.builder().orderNumber(42).build();

        OrderDto order = OrderDto.builder().paymentDto(payment).build();

        assertEquals(payment, order.getPaymentDto());
    }

    @Test
    void buildsPaymentAndCardDtos() {
        PaymentDto payment = PaymentDto.builder().orderNumber(42).totalAmount(new BigDecimal("19.99")).build();
        CardDto card = CardDto.builder().cardHolderName("Alex Doe").cardNumber(1234).expiryDate("12/30").build();

        assertEquals(42, payment.getOrderNumber());
        assertEquals(new BigDecimal("19.99"), payment.getTotalAmount());
        assertEquals("Alex Doe", card.getCardHolderName());
        assertEquals(1234, card.getCardNumber());
    }
}