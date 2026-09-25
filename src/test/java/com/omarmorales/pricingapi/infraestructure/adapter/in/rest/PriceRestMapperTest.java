package com.omarmorales.pricingapi.infraestructure.adapter.in.rest;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.omarmorales.pricingapi.domain.model.DateRange;
import com.omarmorales.pricingapi.domain.model.Money;
import com.omarmorales.pricingapi.domain.model.Price;
import com.omarmorales.pricingapi.infraestructure.adapter.in.rest.dto.PriceResponse;

/**
 * Unit tests for {@link PriceRestMapper}.
 */
class PriceRestMapperTest {

    private static final LocalDateTime START = LocalDateTime.of(2020, 6, 14, 15, 0, 0);
    private static final LocalDateTime END = LocalDateTime.of(2020, 6, 14, 18, 30, 0);

    @Test
    void mapsDomainPriceToResponse() {
        Price price = new Price(1L, 35455L, 2L, new DateRange(START, END), 1,
                Money.of(new BigDecimal("25.45"), "EUR"));

        PriceResponse response = PriceRestMapper.toResponse(price);

        assertThat(response).isEqualTo(new PriceResponse(
                35455L, 1L, 2L, START, END, new BigDecimal("25.45"), "EUR"));
    }
}
