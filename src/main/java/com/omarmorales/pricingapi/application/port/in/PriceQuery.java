package com.omarmorales.pricingapi.application.port.in;

import java.time.LocalDateTime;

/**
 * Search criteria used to look up the price that applies to a product of a brand at a given date.
 *
 * <p>Instances are validated on creation, so a {@code PriceQuery} is always well-formed.
 *
 * @param brandId         identifier of the brand, must be positive
 * @param productId       identifier of the product, must be positive
 * @param applicationDate the date and time at which the price must apply, must not be {@code null}
 * @see FindApplicablePriceUseCase
 */
public record PriceQuery(long brandId, long productId, LocalDateTime applicationDate) {

    /**
     * Creates a query after validating its components.
     *
     * @throws IllegalArgumentException if {@code brandId} or {@code productId} is not positive,
     *                                  or if {@code applicationDate} is {@code null}
     */
    public PriceQuery {
        requirePositive(brandId, "brandId");
        requirePositive(productId, "productId");
        if (applicationDate == null) {
            throw new IllegalArgumentException("applicationDate must not be null");
        }
    }

    /**
     * Ensures that an identifier is strictly greater than zero.
     *
     * @param value the value to check
     * @param name  the component name used in the error message
     * @throws IllegalArgumentException if {@code value} is zero or negative
     */
    private static void requirePositive(long value, String name) {
        if (value <= 0) {
            throw new IllegalArgumentException(name + " must be positive");
        }
    }
}
