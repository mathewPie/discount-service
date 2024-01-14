package com.mpie.discountservice.processor;

import com.mpie.discountservice.factory.DiscountFactory;
import com.mpie.discountservice.model.dto.DiscountDto;
import com.mpie.discountservice.model.dto.OrderDto;
import com.mpie.discountservice.model.dto.OrderItemDto;
import com.mpie.discountservice.model.dto.ProductDiscountDto;
import com.mpie.discountservice.model.entity.DiscountPolicy;
import com.mpie.discountservice.repository.DiscountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class DiscountCalculationFacade {

    private final DiscountRepository discountRepository;
    private final List<DiscountProcessor> discountProcessors;

    public List<ProductDiscountDto> calculateDiscount(OrderDto orderDto) {
        List<ProductDiscountDto> result = new LinkedList<>();
        orderDto.orderItems().forEach(orderItem -> result.add(DiscountFactory.productDiscountDto(orderItem.productId(), calculateDiscountsForProduct(orderItem))));
        return result;
    }

    private List<DiscountDto> calculateDiscountsForProduct(OrderItemDto orderItemDto) {
        log.info("Calculate discounts for product: '{}' ", orderItemDto.productId());
        List<DiscountDto> discounts = new LinkedList<>();
        List<DiscountPolicy> discountPolicies = discountRepository.findByProductId(orderItemDto.productId());
        discountProcessors.forEach(discountProcessor -> {
            List<DiscountPolicy> appliesTo = discountPolicies.stream()
                    .filter(discountProcessor::appliesTo)
                    .toList();

            if (!CollectionUtils.isEmpty(appliesTo)) {
                discounts.add(discountProcessor.calculate(orderItemDto, appliesTo));
            }
        });
        return discounts;
    }

}
