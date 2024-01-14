package com.mpie.discountservice.processor;

import com.mpie.discountservice.model.DiscountType;
import com.mpie.discountservice.model.dto.DiscountDto;
import com.mpie.discountservice.model.dto.OrderDto;
import com.mpie.discountservice.model.dto.OrderItemDto;
import com.mpie.discountservice.model.dto.ProductDiscountDto;
import com.mpie.discountservice.model.entity.CountDiscountPolicy;
import com.mpie.discountservice.model.entity.PercentageDiscountPolicy;
import com.mpie.discountservice.repository.DiscountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DiscountCalculationFacadeTest {

    @Mock
    private DiscountRepository discountRepository;

    @Mock
    private DiscountPercentageProcessor discountPercentageProcessor;

    @Mock
    private DiscountCountProcessor discountCountProcessor;

    @Mock
    private List<DiscountProcessor> discountProcessors;
    @InjectMocks
    private DiscountCalculationFacade discountCalculationFacade;

    @BeforeEach
    void setup() {
        this.discountProcessors = List.of(discountPercentageProcessor, discountCountProcessor);
        ReflectionTestUtils.setField(discountCalculationFacade, "discountProcessors", discountProcessors);
    }


    @Test
    void calculateDiscount_shouldReturnEmptyList() {
        OrderDto emptyOrder = new OrderDto(1L, Collections.emptyList(), null);

        List<ProductDiscountDto> result = discountCalculationFacade.calculateDiscount(emptyOrder);

        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void calculateDiscount_shouldCalculateDiscountsForProducts() {
        OrderItemDto orderItem1 = new OrderItemDto("1", null, 1);
        OrderItemDto orderItem2 = new OrderItemDto("2", null, 2);
        OrderDto orderDto = new OrderDto(1L, Arrays.asList(orderItem1, orderItem2), null);
        CountDiscountPolicy countDiscountPolicy = new CountDiscountPolicy();
        countDiscountPolicy.setDiscountType(DiscountType.COUNT);
        PercentageDiscountPolicy percentageDiscountPolicy = new PercentageDiscountPolicy();
        countDiscountPolicy.setDiscountType(DiscountType.PERCENTAGE);

        when(discountRepository.findByProductId("1")).thenReturn(Collections.singletonList(countDiscountPolicy));
        when(discountRepository.findByProductId("2")).thenReturn(Collections.singletonList(percentageDiscountPolicy));
        when(discountPercentageProcessor.appliesTo(percentageDiscountPolicy)).thenReturn(true);
        when(discountPercentageProcessor.appliesTo(countDiscountPolicy)).thenReturn(false);
        when(discountPercentageProcessor.calculate(any(), any())).thenReturn(new DiscountDto(BigDecimal.valueOf(10), DiscountType.PERCENTAGE));
        when(discountCountProcessor.appliesTo(countDiscountPolicy)).thenReturn(true);
        when(discountCountProcessor.appliesTo(percentageDiscountPolicy)).thenReturn(false);
        when(discountCountProcessor.calculate(any(), any())).thenReturn(new DiscountDto(BigDecimal.valueOf(20), DiscountType.COUNT));

        List<ProductDiscountDto> result = discountCalculationFacade.calculateDiscount(orderDto);

        assertEquals(2, result.size());
        verify(discountPercentageProcessor, times(1)).calculate(any(), any());
        verify(discountCountProcessor, times(1)).calculate(any(), any());
    }

}

