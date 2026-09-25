package com.omarmorales.pricingapi.application.service;

import com.omarmorales.pricingapi.application.port.in.FindApplicablePriceUseCase;
import com.omarmorales.pricingapi.application.port.in.PriceQuery;
import com.omarmorales.pricingapi.application.port.out.PriceRepositoryPort;
import com.omarmorales.pricingapi.domain.exception.ApplicablePriceNotFoundException;
import com.omarmorales.pricingapi.domain.model.Price;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Default implementation of {@link FindApplicablePriceUseCase}.
 *
 * <p>Delegates the lookup to a {@link PriceRepositoryPort} and turns a missing result into an
 * {@link ApplicablePriceNotFoundException}.
 */
@Service
public class FindApplicablePriceService implements FindApplicablePriceUseCase {

    private final PriceRepositoryPort priceRepository;

    /**
     * Creates the service with the repository used to look up prices.
     *
     * @param priceRepository the outbound port used to read prices
     * @throws NullPointerException if {@code priceRepository} is {@code null}
     */
    public FindApplicablePriceService(PriceRepositoryPort priceRepository) {
        this.priceRepository = Objects.requireNonNull(priceRepository, "priceRepository must not be null");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Price findApplicablePrice(PriceQuery query) {
        Objects.requireNonNull(query, "query must not be null");
        return priceRepository.findApplicablePrice(query.brandId(), query.productId(), query.applicationDate())
                .orElseThrow(() -> new ApplicablePriceNotFoundException(query.brandId(), query.productId(), query.applicationDate()));
    }
}
