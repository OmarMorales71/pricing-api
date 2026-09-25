package com.omarmorales.pricingapi.application.port.in;

import com.omarmorales.pricingapi.domain.model.Price;

/**
 * Inbound port for finding the price that applies to a product of a brand at a given date.
 *
 * <p>When several prices overlap at the requested date, the one with the highest priority applies.
 *
 * @see PriceQuery
 */
public interface FindApplicablePriceUseCase {

    /**
     * Finds the price that applies to the given query.
     *
     * @param query the brand, product and application date to search for
     * @return the applicable price, never {@code null}
     * @throws com.omarmorales.pricingapi.domain.exception.ApplicablePriceNotFoundException if no price applies to the query
     */
    Price findApplicablePrice(PriceQuery query);
}
