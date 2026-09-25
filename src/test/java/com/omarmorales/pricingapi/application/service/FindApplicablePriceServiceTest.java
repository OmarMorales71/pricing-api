package com.omarmorales.pricingapi.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.omarmorales.pricingapi.application.port.in.PriceQuery;
import com.omarmorales.pricingapi.application.port.out.PriceRepositoryPort;
import com.omarmorales.pricingapi.domain.exception.ApplicablePriceNotFoundException;
import com.omarmorales.pricingapi.domain.model.DateRange;
import com.omarmorales.pricingapi.domain.model.Money;
import com.omarmorales.pricingapi.domain.model.Price;

/**
 * Unit tests for {@link FindApplicablePriceService}.
 */
@ExtendWith(MockitoExtension.class)
class FindApplicablePriceServiceTest {

    private static final long BRAND_ID = 1L;
    private static final long PRODUCT_ID = 35455L;
    private static final LocalDateTime APPLICATION_DATE = LocalDateTime.of(2020, 6, 14, 16, 0, 0);
    private static final PriceQuery QUERY = new PriceQuery(BRAND_ID, PRODUCT_ID, APPLICATION_DATE);
    private static final Price PRICE = new Price(
            BRAND_ID,
            PRODUCT_ID,
            2L,
            new DateRange(LocalDateTime.of(2020, 6, 14, 15, 0, 0), LocalDateTime.of(2020, 6, 14, 18, 30, 0)),
            1,
            Money.of(new BigDecimal("25.45"), "EUR"));

    @Mock
    private PriceRepositoryPort priceRepository;

    private FindApplicablePriceService service;

    @BeforeEach
    void setUp() {
        service = new FindApplicablePriceService(priceRepository);
    }

    @Nested
    @DisplayName("creation")
    class Creation {

        @Test
        void rejectsNullPriceRepository() {
            assertThatNullPointerException()
                    .isThrownBy(() -> new FindApplicablePriceService(null))
                    .withMessage("priceRepository must not be null");
        }
    }

    @Nested
    @DisplayName("findApplicablePrice")
    class FindApplicablePrice {

        @Test
        void returnsPriceFoundByRepository() {
            given(priceRepository.findApplicablePrice(BRAND_ID, PRODUCT_ID, APPLICATION_DATE))
                    .willReturn(Optional.of(PRICE));

            Price result = service.findApplicablePrice(QUERY);

            assertThat(result).isEqualTo(PRICE);
            verify(priceRepository).findApplicablePrice(BRAND_ID, PRODUCT_ID, APPLICATION_DATE);
        }

        @Test
        void throwsWhenNoPriceApplies() {
            given(priceRepository.findApplicablePrice(BRAND_ID, PRODUCT_ID, APPLICATION_DATE))
                    .willReturn(Optional.empty());

            assertThatThrownBy(() -> service.findApplicablePrice(QUERY))
                    .isInstanceOf(ApplicablePriceNotFoundException.class)
                    .hasMessage("No applicable price found for brand 1 and product 35455 at 2020-06-14T16:00");
        }

        @Test
        void rejectsNullQuery() {
            assertThatNullPointerException()
                    .isThrownBy(() -> service.findApplicablePrice(null))
                    .withMessage("query must not be null");

            verifyNoInteractions(priceRepository);
        }
    }
}
