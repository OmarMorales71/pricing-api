package com.omarmorales.pricingapi.application.port.in;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Unit tests for {@link PriceQuery}.
 */
class PriceQueryTest {

    private static final long BRAND_ID = 1L;
    private static final long PRODUCT_ID = 35455L;
    private static final LocalDateTime APPLICATION_DATE = LocalDateTime.of(2020, 6, 14, 10, 0, 0);

    @Test
    void createsQueryWithGivenValues() {
        PriceQuery query = new PriceQuery(BRAND_ID, PRODUCT_ID, APPLICATION_DATE);

        assertThat(query.brandId()).isEqualTo(BRAND_ID);
        assertThat(query.productId()).isEqualTo(PRODUCT_ID);
        assertThat(query.applicationDate()).isEqualTo(APPLICATION_DATE);
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -1L, Long.MIN_VALUE})
    void rejectsNonPositiveBrandId(long brandId) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new PriceQuery(brandId, PRODUCT_ID, APPLICATION_DATE))
                .withMessage("brandId must be positive");
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -1L, Long.MIN_VALUE})
    void rejectsNonPositiveProductId(long productId) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new PriceQuery(BRAND_ID, productId, APPLICATION_DATE))
                .withMessage("productId must be positive");
    }

    @Test
    void rejectsNullApplicationDate() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new PriceQuery(BRAND_ID, PRODUCT_ID, null))
                .withMessage("applicationDate must not be null");
    }
}
