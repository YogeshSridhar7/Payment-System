package com.macd.ps.service;

import com.macd.ps.entity.Payment;
import com.macd.ps.mapper.PaymentMapper;
import com.macd.ps.model.PaymentDto;
import com.macd.ps.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentMapper paymentMapper;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void createPaymentSetsUtcDateMapsAndSavesPayment() {
        PaymentDto paymentDto = PaymentDto.builder().orderNumber(42).totalAmount(new BigDecimal("19.99")).build();
        Payment entity = Payment.builder().orderNumber(42).totalAmount(new BigDecimal("19.99")).build();
        Payment savedEntity = entity.toBuilder().id(7).build();
        PaymentDto result = paymentDto.toBuilder().id(7).build();
        when(paymentMapper.dtoToEntity(paymentDto)).thenReturn(entity);
        when(paymentRepository.save(entity)).thenReturn(savedEntity);
        when(paymentMapper.entityToDto(savedEntity)).thenReturn(result);
        LocalDateTime before = LocalDateTime.now(ZoneOffset.UTC);

        PaymentDto actual = paymentService.createPayment(paymentDto);

        LocalDateTime after = LocalDateTime.now(ZoneOffset.UTC);
        assertEquals(result, actual);
        assertNotNull(paymentDto.getPaymentDate());
        org.junit.jupiter.api.Assertions.assertFalse(paymentDto.getPaymentDate().isBefore(before));
        org.junit.jupiter.api.Assertions.assertFalse(paymentDto.getPaymentDate().isAfter(after));
        verify(paymentMapper).dtoToEntity(paymentDto);
        verify(paymentRepository).save(entity);
        verify(paymentMapper).entityToDto(savedEntity);
    }

    @Test
    void getPaymentByIdReturnsMappedPayment() {
        Payment entity = Payment.builder().id(7).build();
        PaymentDto result = PaymentDto.builder().id(7).build();
        when(paymentRepository.findById(7L)).thenReturn(Optional.of(entity));
        when(paymentMapper.entityToDto(entity)).thenReturn(result);

        assertEquals(result, paymentService.getPaymentById(7L));
        verify(paymentMapper).entityToDto(entity);
    }

    @Test
    void getPaymentByIdThrowsNotFoundWhenPaymentDoesNotExist() {
        when(paymentRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> paymentService.getPaymentById(99L));

        assertEquals(404, exception.getStatusCode().value());
    }
}