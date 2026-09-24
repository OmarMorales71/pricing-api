package com.omarmorales.pricingapi.domain.exception;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link ApplicablePriceNotFoundException}.
 */
class ApplicablePriceNotFoundExceptionTest {

    @Test
    void buildsMessageFromSearchCriteria() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0, 0);

        ApplicablePriceNotFoundException exception =
                new ApplicablePriceNotFoundException(1L, 35455L, applicationDate);

        assertThat(exception)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("No applicable price found for brand 1 and product 35455 at 2020-06-14T10:00");
    }
}
