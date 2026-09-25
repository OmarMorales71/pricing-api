package com.omarmorales.pricingapi.infrastructure.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.data.domain.Limit;

/**
 * Slice tests for the query declared in {@link PriceJpaRepository}.
 *
 * <p>The table seeded by {@code data.sql} is cleared before each test so every scenario
 * controls its own data.
 */
@DataJpaTest
class PriceJpaRepositoryTest {

    private static final long BRAND_ID = 1L;
    private static final long PRODUCT_ID = 35455L;
    private static final Limit SINGLE_RESULT = Limit.of(1);
    private static final LocalDateTime START = LocalDateTime.of(2020, 6, 14, 15, 0, 0);
    private static final LocalDateTime END = LocalDateTime.of(2020, 6, 14, 18, 30, 0);

    @Autowired
    private PriceJpaRepository priceJpaRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        priceJpaRepository.deleteAllInBatch();
    }

    @Test
    void returnsPriceWhoseRangeContainsApplicationDate() {
        persist(BRAND_ID, PRODUCT_ID, 1L, START, END, 0);

        Optional<PriceJpaEntity> result = findApplicable(BRAND_ID, PRODUCT_ID, START.plusHours(1));

        assertThat(result).map(PriceJpaEntity::getPriceListId).contains(1L);
    }

    @Test
    void includesStartBoundary() {
        persist(BRAND_ID, PRODUCT_ID, 1L, START, END, 0);

        assertThat(findApplicable(BRAND_ID, PRODUCT_ID, START)).isPresent();
    }

    @Test
    void includesEndBoundary() {
        persist(BRAND_ID, PRODUCT_ID, 1L, START, END, 0);

        assertThat(findApplicable(BRAND_ID, PRODUCT_ID, END)).isPresent();
    }

    @Test
    void returnsEmptyBeforeRange() {
        persist(BRAND_ID, PRODUCT_ID, 1L, START, END, 0);

        assertThat(findApplicable(BRAND_ID, PRODUCT_ID, START.minusSeconds(1))).isEmpty();
    }

    @Test
    void returnsEmptyAfterRange() {
        persist(BRAND_ID, PRODUCT_ID, 1L, START, END, 0);

        assertThat(findApplicable(BRAND_ID, PRODUCT_ID, END.plusSeconds(1))).isEmpty();
    }

    @Test
    void returnsEmptyForOtherBrand() {
        persist(BRAND_ID, PRODUCT_ID, 1L, START, END, 0);

        assertThat(findApplicable(2L, PRODUCT_ID, START)).isEmpty();
    }

    @Test
    void returnsEmptyForOtherProduct() {
        persist(BRAND_ID, PRODUCT_ID, 1L, START, END, 0);

        assertThat(findApplicable(BRAND_ID, 99999L, START)).isEmpty();
    }

    @Test
    void returnsHighestPriorityWhenRangesOverlap() {
        persist(BRAND_ID, PRODUCT_ID, 1L, START.minusDays(1), END.plusDays(1), 0);
        persist(BRAND_ID, PRODUCT_ID, 2L, START, END, 1);

        Optional<PriceJpaEntity> result = findApplicable(BRAND_ID, PRODUCT_ID, START.plusHours(1));

        assertThat(result).map(PriceJpaEntity::getPriceListId).contains(2L);
    }

    @Test
    void returnsLatestStartDateWhenPrioritiesTie() {
        persist(BRAND_ID, PRODUCT_ID, 1L, START.minusHours(1), END, 1);
        persist(BRAND_ID, PRODUCT_ID, 2L, START, END, 1);

        Optional<PriceJpaEntity> result = findApplicable(BRAND_ID, PRODUCT_ID, START.plusHours(1));

        assertThat(result).map(PriceJpaEntity::getPriceListId).contains(2L);
    }

    private Optional<PriceJpaEntity> findApplicable(long brandId, long productId, LocalDateTime applicationDate) {
        return priceJpaRepository.findApplicable(brandId, productId, applicationDate, SINGLE_RESULT);
    }

    private void persist(long brandId, long productId, long priceListId,
                         LocalDateTime start, LocalDateTime end, int priority) {
        entityManager.persist(PriceJpaEntity.builder()
                .brandId(brandId)
                .productId(productId)
                .priceListId(priceListId)
                .startDate(start)
                .endDate(end)
                .priority(priority)
                .price(new BigDecimal("25.45"))
                .currency("EUR")
                .build());
        entityManager.flush();
    }
}
