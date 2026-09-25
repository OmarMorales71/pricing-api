package com.omarmorales.pricingapi.infraestructure.adapter.in.rest;

import com.omarmorales.pricingapi.domain.model.Price;
import com.omarmorales.pricingapi.infraestructure.adapter.in.rest.dto.PriceResponse;

final class PriceRestMapper {

    private PriceRestMapper() {
    }

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
