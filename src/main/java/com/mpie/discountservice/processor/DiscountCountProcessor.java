package com.mpie.discountservice.processor;

import com.mpie.discountservice.factory.DiscountFactory;
import com.mpie.discountservice.model.DiscountType;
import com.mpie.discountservice.model.dto.DiscountDto;
import com.mpie.discountservice.model.dto.OrderItemDto;
import com.mpie.discountservice.model.entity.CountDiscountPolicy;
import com.mpie.discountservice.model.entity.DiscountPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class DiscountCountProcessor implements DiscountProcessor {

    @Override
    public boolean appliesTo(DiscountPolicy discountPolicy) {
        return Objects.nonNull(discountPolicy.getDiscountType()) && Objects.equals(discountPolicy.getDiscountType(), DiscountType.COUNT);
    }

    @Override
    public DiscountDto calculate(OrderItemDto orderItemDto, List<DiscountPolicy> discountPolicies) {
        double maxDiscount = discountPolicies.stream()
                .map(discountPolicy -> (CountDiscountPolicy) discountPolicy)
                .filter(policy -> isAllowed(orderItemDto, policy))
                .mapToDouble(DiscountPolicy::getDiscountValue)
                .max().orElse(0);

        return DiscountFactory.discountDto(new BigDecimal(maxDiscount).setScale(2, RoundingMode.HALF_UP), DiscountType.COUNT);
    }

    public boolean isAllowed(OrderItemDto orderItemDto, CountDiscountPolicy discountPolicy) {
        return orderItemDto.quantityOfProductsOrdered() >= discountPolicy.getMinNumberOrdered();
    }

}
