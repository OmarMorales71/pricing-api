package com.omarmorales.pricingapi.infraestructure.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Limit;

import com.omarmorales.pricingapi.domain.model.DateRange;
import com.omarmorales.pricingapi.domain.model.Money;
import com.omarmorales.pricingapi.domain.model.Price;

/**
 * Unit tests for {@link PriceRepositoryAdapter}.
 */
@ExtendWith(MockitoExtension.class)
class PriceRepositoryAdapterTest {

    private static final long BRAND_ID = 1L;
    private static final long PRODUCT_ID = 35455L;
    private static final LocalDateTime APPLICATION_DATE = LocalDateTime.of(2020, 6, 14, 16, 0, 0);
    private static final LocalDateTime START = LocalDateTime.of(2020, 6, 14, 15, 0, 0);
    private static final LocalDateTime END = LocalDateTime.of(2020, 6, 14, 18, 30, 0);
    private static final Limit SINGLE_RESULT = Limit.of(1);

    @Mock
    private PriceJpaRepository priceJpaRepository;

    private PriceRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new PriceRepositoryAdapter(priceJpaRepository);
    }

    @Test
    void returnsMappedPriceWhenOneApplies() {
        PriceJpaEntity entity = PriceJpaEntity.builder()
                .id(2L)
                .brandId(BRAND_ID)
                .productId(PRODUCT_ID)
                .priceListId(2L)
                .startDate(START)
                .endDate(END)
                .priority(1)
                .price(new BigDecimal("25.45"))
                .currency("EUR")
                .build();
        given(priceJpaRepository.findApplicable(BRAND_ID, PRODUCT_ID, APPLICATION_DATE, SINGLE_RESULT))
                .willReturn(Optional.of(entity));

        Optional<Price> result = adapter.findApplicablePrice(BRAND_ID, PRODUCT_ID, APPLICATION_DATE);

        assertThat(result).contains(new Price(
                BRAND_ID,
                PRODUCT_ID,
                2L,
                new DateRange(START, END),
                1,
                Money.of(new BigDecimal("25.45"), "EUR")));
    }

    @Test
    void returnsEmptyWhenNoPriceApplies() {
        given(priceJpaRepository.findApplicable(BRAND_ID, PRODUCT_ID, APPLICATION_DATE, SINGLE_RESULT))
                .willReturn(Optional.empty());

        Optional<Price> result = adapter.findApplicablePrice(BRAND_ID, PRODUCT_ID, APPLICATION_DATE);

        assertThat(result).isEmpty();
    }
}
