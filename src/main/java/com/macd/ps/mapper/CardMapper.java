package com.macd.ps.mapper;

import com.macd.ps.entity.Card;
import com.macd.ps.model.CardDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface CardMapper {
    CardDto entityToDto(Card card);
    Card dtoToEntity(CardDto cardDto);
}
