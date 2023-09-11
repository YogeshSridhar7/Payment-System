package com.macd.ps.mapper;

import com.macd.ps.entity.Payment;
import com.macd.ps.model.PaymentDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface PaymentMapper {
    PaymentDto entityToDto(Payment payment);
    Payment dtoToEntity(PaymentDto paymentDto);
}
