package com.omarmorales.pricingapi.application.port.out;

import com.omarmorales.pricingapi.domain.model.Price;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Outbound port that gives the application layer access to stored prices.
 *
 * <p>Persistence adapters implement this interface; the application layer depends only on it.
 */
public interface PriceRepositoryPort{

    /**
     * Finds the price that applies to a product of a brand at the given date.
     *
     * <p>Implementations must return the highest-priority price whose date range contains
     * {@code applicationDate}.
     *
     * @param brandId         identifier of the brand
     * @param productId       identifier of the product
     * @param applicationDate the date and time at which the price must apply
     * @return the applicable price, or an empty {@link Optional} if none applies
     */
    Optional<Price> findApplicablePrice(long brandId, long productId, LocalDateTime applicationDate);
}
