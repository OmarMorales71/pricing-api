package com.omarmorales.pricingapi.infraestructure.adapter.out.persistence;

import com.omarmorales.pricingapi.application.port.out.PriceRepositoryPort;
import com.omarmorales.pricingapi.domain.model.Price;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class PriceRepositoryAdapter implements PriceRepositoryPort {

    private static final Limit SINGLE_RESULT = Limit.of(1);

    private final PriceJpaRepository priceJpaRepository;
    
    @Override
    public Optional<Price> findApplicablePrice(long brandId, long productId, LocalDateTime applicationDate) {
        return priceJpaRepository.findApplicable(brandId, productId, applicationDate, SINGLE_RESULT)
                .map(PriceEntityMapper::toDomain);
    }
}
