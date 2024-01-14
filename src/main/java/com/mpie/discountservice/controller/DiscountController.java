package com.mpie.discountservice.controller;

import com.mpie.discountservice.model.dto.OrderDto;
import com.mpie.discountservice.model.dto.ProductDiscountDto;
import com.mpie.discountservice.service.DiscountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calculate")
@Validated
public class DiscountController {

    private final DiscountService discountService;

    @Operation(summary = "Calculate discount", tags = "Discount")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Possible discounts calculated",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProductDiscountDto[].class))}),
    })
    @PostMapping
    public List<ProductDiscountDto> calculateDiscount(@RequestBody OrderDto orderDto) {
        return discountService.calculateDiscount(orderDto);
    }

}