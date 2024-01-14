package com.mpie.discountservice.handler;

import com.mpie.discountservice.model.dto.ErrorMessageDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class DiscountServiceExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorMessageDto> illegalArgumentExceptionErrorResponse(IllegalArgumentException illegalArgumentException) {
        return ResponseEntity.badRequest().body(new ErrorMessageDto(illegalArgumentException.getMessage()));
    }
}
