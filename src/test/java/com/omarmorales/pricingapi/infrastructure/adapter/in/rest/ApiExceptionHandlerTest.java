package com.omarmorales.pricingapi.infrastructure.adapter.in.rest;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import com.omarmorales.pricingapi.domain.exception.ApplicablePriceNotFoundException;

/**
 * Unit tests for {@link ApiExceptionHandler}.
 */
class ApiExceptionHandlerTest {

    private final ApiExceptionHandler handler = new ApiExceptionHandler();

    @Test
    void mapsPriceNotFoundToNotFoundProblem() {
        ApplicablePriceNotFoundException exception =
                new ApplicablePriceNotFoundException(1L, 35455L, LocalDateTime.of(2020, 6, 14, 10, 0, 0));

        ProblemDetail problem = handler.handlePriceNotFound(exception);

        assertThat(problem.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(problem.getTitle()).isEqualTo("Price not found");
        assertThat(problem.getDetail()).isEqualTo(exception.getMessage());
    }
}
