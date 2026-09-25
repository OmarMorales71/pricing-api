package com.omarmorales.pricingapi.infraestructure.adapter.in.rest;

import com.omarmorales.pricingapi.domain.exception.ApplicablePriceNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ApplicablePriceNotFoundException.class)
    ProblemDetail handlePriceNotFound(ApplicablePriceNotFoundException exception) {
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        problem.setTitle("Price not found");
        return problem;
    }
}
