package com.omarmorales.pricingapi.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Unit tests for {@link Money}.
 */
class MoneyTest {

    private static final Currency EUR = Currency.getInstance("EUR");

    @Nested
    @DisplayName("creation")
    class Creation {

        @Test
        void createsMoneyWithGivenAmountAndCurrency() {
            Money money = new Money(new BigDecimal("35.50"), EUR);

            assertThat(money.amount()).isEqualByComparingTo("35.50");
            assertThat(money.currency()).isEqualTo(EUR);
        }

        @Test
        void allowsZeroAmount() {
            Money money = new Money(BigDecimal.ZERO, EUR);

            assertThat(money.amount()).isZero();
        }

        @Test
        void rejectsNullAmount() {
            assertThatNullPointerException()
                    .isThrownBy(() -> new Money(null, EUR))
                    .withMessage("amount must not be null");
        }

        @Test
        void rejectsNullCurrency() {
            assertThatNullPointerException()
                    .isThrownBy(() -> new Money(BigDecimal.ONE, null))
                    .withMessage("currency must not be null");
        }

        @Test
        void rejectsNegativeAmount() {
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> new Money(new BigDecimal("-0.01"), EUR))
                    .withMessage("amount must not be negative");
        }
    }

    @Nested
    @DisplayName("of")
    class Of {

        @Test
        void resolvesCurrencyFromIsoCode() {
            Money money = Money.of(new BigDecimal("25.45"), "EUR");

            assertThat(money.amount()).isEqualByComparingTo("25.45");
            assertThat(money.currency()).isEqualTo(EUR);
        }

        @Test
        void rejectsNullCurrencyCode() {
            assertThatNullPointerException()
                    .isThrownBy(() -> Money.of(BigDecimal.ONE, null))
                    .withMessage("currencyCode must not be null");
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "EU", "XYZ1", "eur-"})
        void rejectsInvalidCurrencyCode(String currencyCode) {
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> Money.of(BigDecimal.ONE, currencyCode));
        }

        @Test
        void rejectsNullAmount() {
            assertThatNullPointerException()
                    .isThrownBy(() -> Money.of(null, "EUR"))
                    .withMessage("amount must not be null");
        }

        @Test
        void rejectsNegativeAmount() {
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> Money.of(new BigDecimal("-1"), "EUR"))
                    .withMessage("amount must not be negative");
        }
    }

    @Test
    void equalsWhenAmountAndCurrencyMatch() {
        assertThat(Money.of(new BigDecimal("30.50"), "EUR"))
                .isEqualTo(new Money(new BigDecimal("30.50"), EUR));
    }
}
