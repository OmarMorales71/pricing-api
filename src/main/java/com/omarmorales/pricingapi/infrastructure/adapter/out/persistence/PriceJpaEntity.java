package com.omarmorales.pricingapi.infrastructure.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * JPA entity mapped to the {@code prices} table.
 *
 * <p>Each row is the price of a product of a brand under a price list during a date range.
 * When ranges overlap, the row with the highest {@code priority} applies. The
 * {@code idx_prices_lookup} index supports the applicable-price query.
 */
@Entity
@Table(
        name = "prices",
        indexes = @Index(
                name = "idx_prices_lookup",
                columnList = "brand_id, product_id, end_date, start_date"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriceJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "brand_id", nullable = false)
    private Long brandId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "price_list_id", nullable = false)
    private Long priceListId;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "priority", nullable = false)
    private Integer priority;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

}
