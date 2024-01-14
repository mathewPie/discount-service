package com.mpie.discountservice.service;

import com.mpie.discountservice.model.dto.OrderDto;
import com.mpie.discountservice.model.dto.ProductDiscountDto;
import com.mpie.discountservice.processor.DiscountCalculationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscountService {

    private final DiscountCalculationFacade discountCalculationFacade;

    public List<ProductDiscountDto> calculateDiscount(OrderDto orderDto) {
        return discountCalculationFacade.calculateDiscount(orderDto);
    }
}