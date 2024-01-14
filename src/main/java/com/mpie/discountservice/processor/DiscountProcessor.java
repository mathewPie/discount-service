package com.mpie.discountservice.processor;

import com.mpie.discountservice.model.dto.DiscountDto;
import com.mpie.discountservice.model.dto.OrderItemDto;
import com.mpie.discountservice.model.entity.DiscountPolicy;

import java.util.List;

public interface DiscountProcessor {

    boolean appliesTo(DiscountPolicy discountPolicy);

    DiscountDto calculate(OrderItemDto orderItemDto, List<DiscountPolicy> discountPolicies);
}
