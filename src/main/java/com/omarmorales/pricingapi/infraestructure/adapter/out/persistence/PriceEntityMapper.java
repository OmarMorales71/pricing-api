package com.omarmorales.pricingapi.infraestructure.adapter.out.persistence;

import com.omarmorales.pricingapi.domain.model.DateRange;
import com.omarmorales.pricingapi.domain.model.Money;
import com.omarmorales.pricingapi.domain.model.Price;

final class PriceEntityMapper {

    private PriceEntityMapper() {
    }
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
