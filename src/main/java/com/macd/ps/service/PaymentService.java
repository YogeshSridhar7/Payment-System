package com.macd.ps.service;

import com.macd.ps.mapper.PaymentMapper;
import com.macd.ps.model.PaymentDto;
import com.macd.ps.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    public PaymentDto getPaymentById(Long id) {
        var payment = paymentRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, format("Retry Payment Again for id (%s)", id)
                ));
        return paymentMapper.entityToDto(payment);
    }

    public PaymentDto createPayment(PaymentDto payment) {
        ZonedDateTime ZONED_DATE_TIME = LocalDateTime.now().truncatedTo(ChronoUnit.MICROS).atZone(ZoneId.of("UTC"));
        payment.setPaymentDate(LocalDateTime.from(ZONED_DATE_TIME));
        return paymentMapper.entityToDto(paymentRepository.save(paymentMapper.dtoToEntity(payment)));
    }
}
