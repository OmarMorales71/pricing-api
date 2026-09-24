package com.omarmorales.pricingapi.domain.exception;

import java.time.LocalDateTime;

/**
 * Thrown when no price applies to a product and brand at a given date.
 */
public class ApplicablePriceNotFoundException extends RuntimeException {

    /**
     * Creates the exception for the given search criteria.
     *
     * @param brandId         identifier of the brand
     * @param productId       identifier of the product
     * @param applicationDate the date for which no price was found
     */
    public ApplicablePriceNotFoundException(long brandId, long productId, LocalDateTime applicationDate) {
        super("No applicable price found for brand %d and product %d at %s".formatted(brandId, productId, applicationDate));
    }
}
