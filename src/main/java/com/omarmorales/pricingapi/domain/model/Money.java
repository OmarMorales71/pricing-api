package com.omarmorales.pricingapi.domain.model;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

/**
 * Non-negative monetary amount in a specific currency.
 *
 * @param amount   the amount, zero or positive
 * @param currency the currency of the amount
 */
public record Money(BigDecimal amount, Currency currency) {

    /**
     * Creates a monetary amount.
     *
     * @throws NullPointerException     if {@code amount} or {@code currency} is {@code null}
     * @throws IllegalArgumentException if {@code amount} is negative
     */
    public Money {
        Objects.requireNonNull(amount, "amount must not be null");
        Objects.requireNonNull(currency, "currency must not be null");

        if (amount.signum() < 0) {
            throw new IllegalArgumentException("amount must not be negative");
        }
    }

    /**
     * Creates a monetary amount from an ISO 4217 currency code.
     *
     * @param amount       the amount, zero or positive
     * @param currencyCode the ISO 4217 currency code, for example {@code "EUR"}
     * @return a new {@code Money} instance
     * @throws NullPointerException     if {@code amount} or {@code currencyCode} is {@code null}
     * @throws IllegalArgumentException if {@code currencyCode} is not a valid ISO 4217 code,
     *                                  or if {@code amount} is negative
     */
    public static Money of(BigDecimal amount, String currencyCode) {
        Objects.requireNonNull(currencyCode, "currencyCode must not be null");
        return new Money(amount, Currency.getInstance(currencyCode));
    }
}
