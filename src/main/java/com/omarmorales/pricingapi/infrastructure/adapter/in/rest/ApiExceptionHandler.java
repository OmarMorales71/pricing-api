package com.omarmorales.pricingapi.infrastructure.adapter.in.rest;

import com.omarmorales.pricingapi.domain.exception.ApplicablePriceNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Translates exceptions raised by the REST adapters into RFC 9457 problem details.
 *
 * <p>Standard Spring MVC errors, such as missing or malformed request parameters, are handled
 * by {@link ResponseEntityExceptionHandler}.
 */
@RestControllerAdvice
class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Maps an {@link ApplicablePriceNotFoundException} to a {@code 404 Not Found} problem.
     *
     * @param exception the exception raised when no price applies
     * @return the problem detail describing the missing price
     */
    @ExceptionHandler(ApplicablePriceNotFoundException.class)
    ProblemDetail handlePriceNotFound(ApplicablePriceNotFoundException exception) {
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        problem.setTitle("Price not found");
        return problem;
    }

    /**
     * Maps an {@link IllegalArgumentException} to a {@code 400 Bad Request} problem.
     *
     * @param exception the exception raised when an argument is invalid
     * @return the problem detail describing the invalid argument
     */
    @ExceptionHandler(IllegalArgumentException.class)
    ProblemDetail handleIllegalArgument(IllegalArgumentException exception) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        problem.setTitle("Invalid request");
        return problem;
    }
}
