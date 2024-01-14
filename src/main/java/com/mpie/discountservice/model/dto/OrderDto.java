package com.mpie.discountservice.model.dto;

import java.util.List;

public record OrderDto(Long orderId, List<OrderItemDto> orderItems, PriceDto price) {
}
