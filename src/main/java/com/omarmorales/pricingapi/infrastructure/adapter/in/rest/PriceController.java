package com.omarmorales.pricingapi.infrastructure.adapter.in.rest;

import com.omarmorales.pricingapi.application.port.in.FindApplicablePriceUseCase;
import com.omarmorales.pricingapi.application.port.in.PriceQuery;
import com.omarmorales.pricingapi.infrastructure.adapter.in.rest.dto.PriceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * REST adapter that exposes the applicable-price query under {@code /api/v1/prices}.
 */
@RestController
@RequestMapping(path = "/api/v1/prices", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class PriceController {

    private final FindApplicablePriceUseCase findApplicablePriceUseCase;

    /**
     * Returns the price that applies to a product of a brand at the given date.
     *
     * <p>Responds with {@code 404 Not Found} when no price applies and with
     * {@code 400 Bad Request} when a parameter is missing or malformed.
     *
     * @param brandId         identifier of the brand
     * @param productId       identifier of the product
     * @param applicationDate the date and time at which the price must apply, in ISO-8601 format
     *                        (for example {@code 2020-06-14T16:00:00})
     * @return {@code 200 OK} with the applicable price
     */
    @GetMapping
    public ResponseEntity<PriceResponse> findApplicablePrice(
            @RequestParam long brandId,
            @RequestParam long productId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime applicationDate) {
        return ResponseEntity.ok(PriceRestMapper.toResponse(
                findApplicablePriceUseCase.findApplicablePrice(
                        new PriceQuery(brandId, productId, applicationDate))));
    }
}
