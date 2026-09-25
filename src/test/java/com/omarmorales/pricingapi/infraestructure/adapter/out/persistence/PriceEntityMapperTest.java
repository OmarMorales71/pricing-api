package com.omarmorales.pricingapi.infraestructure.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.omarmorales.pricingapi.domain.model.DateRange;
import com.omarmorales.pricingapi.domain.model.Money;
import com.omarmorales.pricingapi.domain.model.Price;

/**
 * Unit tests for {@link PriceEntityMapper}.
 */
class PriceEntityMapperTest {

    private static final long BRAND_ID = 1L;
    private static final long PRODUCT_ID = 35455L;
    private static final long PRICE_LIST_ID = 2L;
    private static final int PRIORITY = 1;
    private static final LocalDateTime START = LocalDateTime.of(2020, 6, 14, 15, 0, 0);
    private static final LocalDateTime END = LocalDateTime.of(2020, 6, 14, 18, 30, 0);
    private static final BigDecimal AMOUNT = new BigDecimal("25.45");
    private static final String CURRENCY = "EUR";

    @Test
    void mapsEntityToDomainPrice() {
        PriceJpaEntity entity = entityBuilder().build();

        Price price = PriceEntityMapper.toDomain(entity);

        assertThat(price).isEqualTo(new Price(
                BRAND_ID,
                PRODUCT_ID,
                PRICE_LIST_ID,
                new DateRange(START, END),
                PRIORITY,
                Money.of(AMOUNT, CURRENCY)));
    }

    @Test
    void rejectsEntityWithInvalidCurrency() {
        PriceJpaEntity entity = entityBuilder().currency("XXXX").build();

        assertThatIllegalArgumentException()
                .isThrownBy(() -> PriceEntityMapper.toDomain(entity));
    }

    @Test
    void rejectsEntityWithEndDateBeforeStartDate() {
        PriceJpaEntity entity = entityBuilder().startDate(END).endDate(START).build();

        assertThatIllegalArgumentException()
                .isThrownBy(() -> PriceEntityMapper.toDomain(entity));
    }

    private static PriceJpaEntity.PriceJpaEntityBuilder entityBuilder() {
        return PriceJpaEntity.builder()
                .id(10L)
                .brandId(BRAND_ID)
                .productId(PRODUCT_ID)
                .priceListId(PRICE_LIST_ID)
                .startDate(START)
                .endDate(END)
                .priority(PRIORITY)
                .price(AMOUNT)
                .currency(CURRENCY);
    }
}
