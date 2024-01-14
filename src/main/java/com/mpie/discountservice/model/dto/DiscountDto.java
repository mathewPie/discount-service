package com.mpie.discountservice.model.dto;

import com.mpie.discountservice.model.DiscountType;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record DiscountDto(BigDecimal grossDiscount, DiscountType discountType) {
}
