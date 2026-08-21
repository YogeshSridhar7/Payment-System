package com.macd.ps.mapper;

import com.macd.ps.entity.Payment;
import com.macd.ps.model.PaymentDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PaymentMapperTest {

    private final PaymentMapper paymentMapper = Mappers.getMapper(PaymentMapper.class);

    @Test
    void mapsEntityToDto() {
        Payment payment = Payment.builder().id(7).orderNumber(42).totalAmount(new BigDecimal("19.99")).build();

        PaymentDto result = paymentMapper.entityToDto(payment);

        assertEquals(payment.getId(), result.getId());
        assertEquals(payment.getOrderNumber(), result.getOrderNumber());
        assertEquals(payment.getTotalAmount(), result.getTotalAmount());
    }

    @Test
    void mapsDtoToEntityAndLeavesUnsupportedCardUnset() {
        PaymentDto paymentDto = PaymentDto.builder().id(7).orderNumber(42).totalAmount(new BigDecimal("19.99")).build();

        Payment result = paymentMapper.dtoToEntity(paymentDto);

        assertEquals(paymentDto.getId(), result.getId());
        assertEquals(paymentDto.getOrderNumber(), result.getOrderNumber());
        assertEquals(paymentDto.getTotalAmount(), result.getTotalAmount());
        assertNull(result.getCard());
    }
}