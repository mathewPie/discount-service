package com.mpie.discountservice.processor;

import com.mpie.discountservice.model.DiscountType;
import com.mpie.discountservice.model.dto.DiscountDto;
import com.mpie.discountservice.model.dto.OrderItemDto;
import com.mpie.discountservice.model.entity.CountDiscountPolicy;
import com.mpie.discountservice.model.entity.DiscountPolicy;
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
class DiscountCountProcessorTest {

    @InjectMocks
    private DiscountCountProcessor discountCountProcessor;

    @Mock
    private OrderItemDto orderItemDto;

    @Mock
    private CountDiscountPolicy discountPolicy1;

    @Mock
    private CountDiscountPolicy discountPolicy2;

    @Mock
    private DiscountPolicy discountPolicyPercentage;

    @Test
    void appliesTo_shouldReturnTrueForCountDiscountPolicy() {
        when(discountPolicy1.getDiscountType()).thenReturn(DiscountType.COUNT);
        assertTrue(discountCountProcessor.appliesTo(discountPolicy1));
    }

    @Test
    void appliesTo_shouldReturnFalseForOtherDiscountPolicyTypes() {
        when(discountPolicyPercentage.getDiscountType()).thenReturn(DiscountType.PERCENTAGE);
        assertFalse(discountCountProcessor.appliesTo(discountPolicyPercentage));
    }

    @Test
    void calculate_shouldReturnProductDiscountDtoWithMaxDiscount() {
        when(orderItemDto.quantityOfProductsOrdered()).thenReturn(5);

        when(discountPolicy1.getMinNumberOrdered()).thenReturn(3);
        when(discountPolicy1.getDiscountValue()).thenReturn(10.0);

        when(discountPolicy2.getMinNumberOrdered()).thenReturn(4);
        when(discountPolicy2.getDiscountValue()).thenReturn(15.0);

        List<DiscountPolicy> discountPolicies = Arrays.asList(discountPolicy1, discountPolicy2);

        DiscountDto expectedDiscountDto = new DiscountDto(new BigDecimal("15.00"), DiscountType.COUNT);

        DiscountDto result = discountCountProcessor.calculate(orderItemDto, discountPolicies);

        assertEquals(expectedDiscountDto, result);
    }

    @Test
    void calculate_shouldReturnProductDiscountDtoWithZeroDiscountWhenNoApplicableDiscountPolicy() {
        when(orderItemDto.quantityOfProductsOrdered()).thenReturn(2);

        List<DiscountPolicy> discountPolicies = Collections.singletonList(discountPolicy1);

        DiscountDto result = discountCountProcessor.calculate(orderItemDto, discountPolicies);

        DiscountDto expectedDiscountDto = new DiscountDto(BigDecimal.ZERO.setScale(2), DiscountType.COUNT);
        assertEquals(expectedDiscountDto, result);
    }

    @Test
    void isAllowed_shouldReturnTrueWhenQuantityIsEqualOrGreaterThanMinNumberOrdered() {
        when(orderItemDto.quantityOfProductsOrdered()).thenReturn(5);
        when(discountPolicy1.getMinNumberOrdered()).thenReturn(3);

        assertTrue(discountCountProcessor.isAllowed(orderItemDto, discountPolicy1));
    }

    @Test
    void isAllowed_shouldReturnFalseWhenQuantityIsLessThanMinNumberOrdered() {
        when(orderItemDto.quantityOfProductsOrdered()).thenReturn(2);
        when(discountPolicy1.getMinNumberOrdered()).thenReturn(3);

        assertFalse(discountCountProcessor.isAllowed(orderItemDto, discountPolicy1));
    }

}

