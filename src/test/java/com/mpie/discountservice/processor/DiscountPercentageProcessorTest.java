package com.mpie.discountservice.processor;

import com.mpie.discountservice.model.DiscountType;
import com.mpie.discountservice.model.dto.DiscountDto;
import com.mpie.discountservice.model.dto.OrderItemDto;
import com.mpie.discountservice.model.dto.PriceDto;
import com.mpie.discountservice.model.entity.DiscountPolicy;
import com.mpie.discountservice.model.entity.PercentageDiscountPolicy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DiscountPercentageProcessorTest {

    @InjectMocks
    private DiscountPercentageProcessor discountPercentageProcessor;

    @Mock
    private OrderItemDto orderItemDto;

    @Mock
    private DiscountPolicy discountPolicy;

    @Test
    void appliesTo_shouldReturnTrueForPercentageDiscountPolicy() {
        when(discountPolicy.getDiscountType()).thenReturn(DiscountType.PERCENTAGE);
        assertTrue(discountPercentageProcessor.appliesTo(discountPolicy));
    }

    @Test
    void appliesTo_shouldReturnFalseForOtherDiscountPolicyTypes() {
        when(discountPolicy.getDiscountType()).thenReturn(DiscountType.COUNT);
        assertFalse(discountPercentageProcessor.appliesTo(discountPolicy));
    }

    @Test
    void calculate_shouldReturnProductDiscountDtoWithMaxPercentageDiscount() {
        when(orderItemDto.productPrice()).thenReturn(PriceDto.builder().grossPrice(new BigDecimal("100.0")).build());

        PercentageDiscountPolicy percentageDiscountPolicy = new PercentageDiscountPolicy();
        PercentageDiscountPolicy percentageDiscountPolicy2 = new PercentageDiscountPolicy();
        percentageDiscountPolicy.setDiscountValue(10.0);
        percentageDiscountPolicy2.setDiscountValue(10.0);

        List<DiscountPolicy> discountPolicies = Arrays.asList(percentageDiscountPolicy, percentageDiscountPolicy2);

        DiscountDto expectedDiscountDto = new DiscountDto(new BigDecimal("10.0").setScale(2), DiscountType.PERCENTAGE);

        DiscountDto result = discountPercentageProcessor.calculate(orderItemDto, discountPolicies);

        assertEquals(expectedDiscountDto, result);
    }

    @Test
    void calculate_shouldReturnProductDiscountDtoWithZeroDiscountWhenNoApplicableDiscountPolicy() {
        when(orderItemDto.productPrice()).thenReturn(new PriceDto(BigDecimal.ZERO));

        List<DiscountPolicy> discountPolicies = Collections.singletonList(discountPolicy);

        DiscountDto result = discountPercentageProcessor.calculate(orderItemDto, discountPolicies);

        DiscountDto expectedDiscountDto = new DiscountDto(BigDecimal.ZERO.setScale(2), DiscountType.PERCENTAGE);
        assertEquals(expectedDiscountDto, result);
    }

}
