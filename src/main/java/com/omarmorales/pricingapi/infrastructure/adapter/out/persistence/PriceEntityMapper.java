package com.omarmorales.pricingapi.infrastructure.adapter.out.persistence;

import com.omarmorales.pricingapi.domain.model.DateRange;
import com.omarmorales.pricingapi.domain.model.Money;
import com.omarmorales.pricingapi.domain.model.Price;

/**
 * Maps {@link PriceJpaEntity} instances to the {@link Price} domain model.
 */
final class PriceEntityMapper {

    private PriceEntityMapper() {
    }

    /**
     * Converts a persisted price into a domain price.
     *
     * @param entity the persisted price
     * @return the equivalent domain price
     * @throws IllegalArgumentException if the stored data breaks a domain rule, such as an
     *                                  unknown currency code or an end date before the start date
     */
    static Price toDomain(PriceJpaEntity entity) {
        return new Price(
                entity.getBrandId(),
                entity.getProductId(),
                entity.getPriceListId(),
                new DateRange(entity.getStartDate(), entity.getEndDate()),
                entity.getPriority(),
                Money.of(entity.getPrice(), entity.getCurrency()));
    }
}
