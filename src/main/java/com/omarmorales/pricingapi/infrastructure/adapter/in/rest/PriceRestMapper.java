package com.omarmorales.pricingapi.infrastructure.adapter.in.rest;

import com.omarmorales.pricingapi.domain.model.Price;
import com.omarmorales.pricingapi.infrastructure.adapter.in.rest.dto.PriceResponse;

/**
 * Maps the {@link Price} domain model to the {@link PriceResponse} REST representation.
 */
final class PriceRestMapper {

    private PriceRestMapper() {
    }

    /**
     * Converts a domain price into its REST response.
     *
     * @param price the domain price
     * @return the response body for the price
     */
    static PriceResponse toResponse(Price price) {
        return new PriceResponse(
                price.productId(),
                price.brandId(),
                price.priceListId(),
                price.applicationPeriod().start(),
                price.applicationPeriod().end(),
                price.amount().amount(),
                price.amount().currency().getCurrencyCode());
    }
}
