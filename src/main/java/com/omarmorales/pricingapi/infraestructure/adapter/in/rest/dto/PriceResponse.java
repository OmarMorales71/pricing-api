package com.omarmorales.pricingapi.infraestructure.adapter.in.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PriceResponse(
        long productId,
        long brandId,
        long applicableRate,  // Identifier of the price list that applies
        LocalDateTime startDate,
        LocalDateTime endDate,
        BigDecimal price,
        String currency) {
}
