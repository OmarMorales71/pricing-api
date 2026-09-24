package com.omarmorales.pricingapi.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Price of a product for a brand, valid during a given period.
 * <p>
 * Several prices may overlap in time for the same product and brand; in that case
 * the one with the highest {@code priority} is the one that applies.
 *
 * @param brandId           identifier of the brand, must be positive
 * @param productId         identifier of the product, must be positive
 * @param priceListId       identifier of the price list this price belongs to, must be positive
 * @param applicationPeriod period during which this price is valid
 * @param priority          disambiguator for overlapping prices, zero or positive; the higher value wins
 * @param amount            final price and its currency
 */
public record Price(
        long brandId,
        long productId,
        long priceListId,
        DateRange applicationPeriod,
        int priority,
        Money amount) {

    /**
     * Creates a price.
     *
     * @throws NullPointerException     if {@code applicationPeriod} or {@code amount} is {@code null}
     * @throws IllegalArgumentException if any identifier is not positive or {@code priority} is negative
     */

    // brandId, productId, priceListId must be positive? can they be zero? or negative?
    // priority must be zero or positive? can be negative?
    public Price {
        requirePositive(brandId, "brandId");
        requirePositive(productId, "productId");
        requirePositive(priceListId, "priceListId");
        if (priority < 0) {
            throw new IllegalArgumentException("priority must not be negative");
        }
        Objects.requireNonNull(applicationPeriod, "applicationPeriod must not be null");
        Objects.requireNonNull(amount, "amount must not be null");
    }

    /**
     * Checks whether this price is valid at the given date.
     * <p>
     * This only considers the application period; resolving overlaps by priority
     * is the caller's responsibility.
     *
     * @param applicationDate the date to check
     * @return {@code true} if {@code applicationDate} falls within the application period
     * @throws NullPointerException if {@code applicationDate} is {@code null}
     */
    public boolean isApplicable(LocalDateTime applicationDate) {
        Objects.requireNonNull(applicationDate, "applicationDate must not be null");
        return applicationPeriod.contains(applicationDate);
    }

    private static void requirePositive(long value, String name) {
        if (value <= 0) {
            throw new IllegalArgumentException(name + " must be positive");
        }
    }
}
