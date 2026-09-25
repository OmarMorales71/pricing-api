package com.omarmorales.pricingapi.infrastructure.adapter.out.persistence;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Spring Data repository for {@link PriceJpaEntity}.
 */
public interface PriceJpaRepository extends JpaRepository<PriceJpaEntity, Long> {

    /**
     * Finds the prices of a product of a brand whose date range contains the given date.
     *
     * <p>Both range boundaries are inclusive. Results are ordered by priority, highest first,
     * and then by start date, latest first, so a {@link Limit} of one returns the applicable price.
     *
     * @param brandId         identifier of the brand
     * @param productId       identifier of the product
     * @param applicationDate the date and time at which the price must apply
     * @param limit           the maximum number of results to return
     * @return the first matching price, or an empty {@link Optional} if none matches
     */
    @Query("""
            SELECT p
            FROM PriceJpaEntity p
            WHERE p.brandId = :brandId
              AND p.productId = :productId
              AND :applicationDate BETWEEN p.startDate AND p.endDate
            ORDER BY p.priority DESC, p.startDate DESC
            """)
    Optional<PriceJpaEntity> findApplicable(
            @Param("brandId") long brandId,
            @Param("productId") long productId,
            @Param("applicationDate") LocalDateTime applicationDate,
            Limit limit);
}
