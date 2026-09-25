package com.omarmorales.pricingapi.infraestructure.adapter.in.rest;

import com.omarmorales.pricingapi.application.port.in.FindApplicablePriceUseCase;
import com.omarmorales.pricingapi.application.port.in.PriceQuery;
import com.omarmorales.pricingapi.infraestructure.adapter.in.rest.dto.PriceResponse;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping(path = "/api/v1/prices", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class PriceController {

    private final FindApplicablePriceUseCase findApplicablePriceUseCase;

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
