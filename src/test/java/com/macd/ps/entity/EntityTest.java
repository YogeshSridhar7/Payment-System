package com.macd.ps.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class EntityTest {

    @Test
    void buildsCardEntity() {
        Card card = Card.builder().cardHolderName("Alex Doe").cardNumber(1234).expiryDate("12/30").build();

        assertEquals("Alex Doe", card.getCardHolderName());
        assertEquals(1234, card.getCardNumber());
        assertEquals("12/30", card.getExpiryDate());
    }

    @Test
    void buildsPaymentEntityWithCard() {
        Card card = Card.builder().id(3).build();
        Payment payment = Payment.builder().id(7).orderNumber(42).totalAmount(new BigDecimal("19.99")).card(card).build();

        assertEquals(7, payment.getId());
        assertEquals(42, payment.getOrderNumber());
        assertEquals(new BigDecimal("19.99"), payment.getTotalAmount());
        assertSame(card, payment.getCard());
    }
}