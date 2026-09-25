package com.omarmorales.pricingapi.infrastructure.adapter.in.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response body returned by the applicable-price endpoint.
 *
 * @param productId      identifier of the product
 * @param brandId        identifier of the brand
 * @param applicableRate identifier of the price list that applies
 * @param startDate      start of the period in which the price applies
 * @param endDate        end of the period in which the price applies
 * @param price          the final price
 * @param currency       the ISO 4217 currency code of the price
 */
public record PriceResponse(
        long productId,
        long brandId,
        long applicableRate,
        LocalDateTime startDate,
        LocalDateTime endDate,
        BigDecimal price,
        String currency) {}
