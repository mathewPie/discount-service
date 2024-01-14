package com.mpie.discountservice.model.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record PriceDto(BigDecimal grossPrice) {

}
