package com.mpie.discountservice.model.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ProductDiscountDto(String productId, List<DiscountDto> discounts) {
}
