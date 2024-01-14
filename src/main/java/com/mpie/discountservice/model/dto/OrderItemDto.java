package com.mpie.discountservice.model.dto;

import lombok.Builder;

@Builder
public record OrderItemDto(String productId, PriceDto productPrice, Integer quantityOfProductsOrdered) {
}
