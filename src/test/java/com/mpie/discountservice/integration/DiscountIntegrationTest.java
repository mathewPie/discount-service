package com.mpie.discountservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpie.discountservice.model.DiscountType;
import com.mpie.discountservice.model.dto.OrderDto;
import com.mpie.discountservice.model.dto.OrderItemDto;
import com.mpie.discountservice.model.dto.PriceDto;
import com.mpie.discountservice.model.entity.CountDiscountPolicy;
import com.mpie.discountservice.model.entity.PercentageDiscountPolicy;
import com.mpie.discountservice.repository.DiscountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class DiscountIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DiscountRepository discountRepository;

    @Test
    void calculateDiscount_shouldReturnDiscounts() throws Exception {
        List<OrderItemDto> orderItems = List.of(
                OrderItemDto.builder()
                        .productPrice(PriceDto.builder()
                                .grossPrice(new BigDecimal("100"))
                                .build())
                        .quantityOfProductsOrdered(10)
                        .productId("123")
                        .build()
        );
        OrderDto orderDto = new OrderDto(1L, orderItems, PriceDto.builder().grossPrice(new BigDecimal("1000")).build());
        discountRepository.save(createPercentageDiscountPolicy("123", 10D));
        discountRepository.save(createPercentageDiscountPolicy("123", 15D));
        discountRepository.save(createCountDiscountPolicy("123", 15D, 5));
        discountRepository.save(createCountDiscountPolicy("123", 2D, 7));

        mockMvc.perform(post("/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].productId").value("123"))
                .andExpect(jsonPath("$[0].discounts[0].grossDiscount").value(15.00))
                .andExpect(jsonPath("$[0].discounts[1].grossDiscount").value(15.00))
                .andExpect(jsonPath("$[0].discounts[0].discountType").value("COUNT"))
                .andExpect(jsonPath("$[0].discounts[1].discountType").value("PERCENTAGE"));
    }

    private PercentageDiscountPolicy createPercentageDiscountPolicy(String productId, Double discountValue) {
        var percentagePolicy = new PercentageDiscountPolicy();
        percentagePolicy.setDiscountValue(discountValue);
        percentagePolicy.setDiscountType(DiscountType.PERCENTAGE);
        percentagePolicy.setProductId(productId);
        return percentagePolicy;
    }

    private CountDiscountPolicy createCountDiscountPolicy(String productId, Double discountValue, int minimumNumberOrdered) {
        var countDiscountPolicy = new CountDiscountPolicy();
        countDiscountPolicy.setDiscountValue(discountValue);
        countDiscountPolicy.setDiscountType(DiscountType.COUNT);
        countDiscountPolicy.setMinNumberOrdered(minimumNumberOrdered);
        countDiscountPolicy.setProductId(productId);
        return countDiscountPolicy;
    }
}
