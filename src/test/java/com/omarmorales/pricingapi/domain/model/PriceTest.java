package com.omarmorales.pricingapi.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Unit tests for {@link Price}.
 */
class PriceTest {

    private static final long BRAND_ID = 1L;
    private static final long PRODUCT_ID = 35455L;
    private static final long PRICE_LIST_ID = 2L;
    private static final int PRIORITY = 1;
    private static final LocalDateTime START = LocalDateTime.of(2020, 6, 14, 15, 0, 0);
    private static final LocalDateTime END = LocalDateTime.of(2020, 6, 14, 18, 30, 0);
    private static final DateRange PERIOD = new DateRange(START, END);
    private static final Money AMOUNT = Money.of(new BigDecimal("25.45"), "EUR");

    @Nested
    @DisplayName("creation")
    class Creation {

        @Test
        void createsPriceWithGivenValues() {
            Price price = new Price(BRAND_ID, PRODUCT_ID, PRICE_LIST_ID, PERIOD, PRIORITY, AMOUNT);

            assertThat(price.brandId()).isEqualTo(BRAND_ID);
            assertThat(price.productId()).isEqualTo(PRODUCT_ID);
            assertThat(price.priceListId()).isEqualTo(PRICE_LIST_ID);
            assertThat(price.applicationPeriod()).isEqualTo(PERIOD);
            assertThat(price.priority()).isEqualTo(PRIORITY);
            assertThat(price.amount()).isEqualTo(AMOUNT);
        }

        @Test
        void allowsZeroPriority() {
            Price price = new Price(BRAND_ID, PRODUCT_ID, PRICE_LIST_ID, PERIOD, 0, AMOUNT);

            assertThat(price.priority()).isZero();
        }

        @ParameterizedTest
        @ValueSource(longs = {0L, -1L, Long.MIN_VALUE})
        void rejectsNonPositiveBrandId(long brandId) {
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> new Price(brandId, PRODUCT_ID, PRICE_LIST_ID, PERIOD, PRIORITY, AMOUNT))
                    .withMessage("brandId must be positive");
        }

        @ParameterizedTest
        @ValueSource(longs = {0L, -1L, Long.MIN_VALUE})
        void rejectsNonPositiveProductId(long productId) {
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> new Price(BRAND_ID, productId, PRICE_LIST_ID, PERIOD, PRIORITY, AMOUNT))
                    .withMessage("productId must be positive");
        }

        @ParameterizedTest
        @ValueSource(longs = {0L, -1L, Long.MIN_VALUE})
        void rejectsNonPositivePriceListId(long priceListId) {
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> new Price(BRAND_ID, PRODUCT_ID, priceListId, PERIOD, PRIORITY, AMOUNT))
                    .withMessage("priceListId must be positive");
        }

        @ParameterizedTest
        @ValueSource(ints = {-1, Integer.MIN_VALUE})
        void rejectsNegativePriority(int priority) {
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> new Price(BRAND_ID, PRODUCT_ID, PRICE_LIST_ID, PERIOD, priority, AMOUNT))
                    .withMessage("priority must not be negative");
        }

        @Test
        void rejectsNullApplicationPeriod() {
            assertThatNullPointerException()
                    .isThrownBy(() -> new Price(BRAND_ID, PRODUCT_ID, PRICE_LIST_ID, null, PRIORITY, AMOUNT))
                    .withMessage("applicationPeriod must not be null");
        }

        @Test
        void rejectsNullAmount() {
            assertThatNullPointerException()
                    .isThrownBy(() -> new Price(BRAND_ID, PRODUCT_ID, PRICE_LIST_ID, PERIOD, PRIORITY, null))
                    .withMessage("amount must not be null");
        }
    }

    @Nested
    @DisplayName("isApplicable")
    class IsApplicable {

        private final Price price = new Price(BRAND_ID, PRODUCT_ID, PRICE_LIST_ID, PERIOD, PRIORITY, AMOUNT);

        @Test
        void appliesAtStartOfPeriod() {
            assertThat(price.isApplicable(START)).isTrue();
        }

        @Test
        void appliesAtEndOfPeriod() {
            assertThat(price.isApplicable(END)).isTrue();
        }

        @Test
        void appliesWithinPeriod() {
            assertThat(price.isApplicable(LocalDateTime.of(2020, 6, 14, 16, 0))).isTrue();
        }

        @Test
        void doesNotApplyBeforePeriod() {
            assertThat(price.isApplicable(START.minusSeconds(1))).isFalse();
        }

        @Test
        void doesNotApplyAfterPeriod() {
            assertThat(price.isApplicable(END.plusSeconds(1))).isFalse();
        }

        @Test
        void rejectsNullApplicationDate() {
            assertThatNullPointerException()
                    .isThrownBy(() -> price.isApplicable(null))
                    .withMessage("applicationDate must not be null");
        }
    }
}
