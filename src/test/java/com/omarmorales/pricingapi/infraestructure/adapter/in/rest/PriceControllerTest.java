package com.omarmorales.pricingapi.infraestructure.adapter.in.rest;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.omarmorales.pricingapi.application.port.in.FindApplicablePriceUseCase;
import com.omarmorales.pricingapi.application.port.in.PriceQuery;
import com.omarmorales.pricingapi.domain.exception.ApplicablePriceNotFoundException;
import com.omarmorales.pricingapi.domain.model.DateRange;
import com.omarmorales.pricingapi.domain.model.Money;
import com.omarmorales.pricingapi.domain.model.Price;

/**
 * Web slice tests for {@link PriceController} and {@link ApiExceptionHandler}.
 */
@WebMvcTest(PriceController.class)
class PriceControllerTest {

    private static final String PRICES_PATH = "/api/v1/prices";
    private static final long BRAND_ID = 1L;
    private static final long PRODUCT_ID = 35455L;
    private static final LocalDateTime APPLICATION_DATE = LocalDateTime.of(2020, 6, 14, 16, 0, 0);
    private static final PriceQuery QUERY = new PriceQuery(BRAND_ID, PRODUCT_ID, APPLICATION_DATE);

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FindApplicablePriceUseCase findApplicablePriceUseCase;

    @Test
    void returnsApplicablePrice() throws Exception {
        Price price = new Price(
                BRAND_ID,
                PRODUCT_ID,
                2L,
                new DateRange(LocalDateTime.of(2020, 6, 14, 15, 0, 0), LocalDateTime.of(2020, 6, 14, 18, 30, 0)),
                1,
                Money.of(new BigDecimal("25.45"), "EUR"));
        given(findApplicablePriceUseCase.findApplicablePrice(QUERY)).willReturn(price);

        mockMvc.perform(get(PRICES_PATH)
                        .param("brandId", "1")
                        .param("productId", "35455")
                        .param("applicationDate", "2020-06-14T16:00:00"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.productId").value(35455))
                .andExpect(jsonPath("$.brandId").value(1))
                .andExpect(jsonPath("$.applicableRate").value(2))
                .andExpect(jsonPath("$.startDate").value("2020-06-14T15:00:00"))
                .andExpect(jsonPath("$.endDate").value("2020-06-14T18:30:00"))
                .andExpect(jsonPath("$.price").value(25.45))
                .andExpect(jsonPath("$.currency").value("EUR"));
    }

    @Test
    void returnsNotFoundProblemWhenNoPriceApplies() throws Exception {
        given(findApplicablePriceUseCase.findApplicablePrice(QUERY))
                .willThrow(new ApplicablePriceNotFoundException(BRAND_ID, PRODUCT_ID, APPLICATION_DATE));

        mockMvc.perform(get(PRICES_PATH)
                        .param("brandId", "1")
                        .param("productId", "35455")
                        .param("applicationDate", "2020-06-14T16:00:00"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.title").value("Price not found"))
                .andExpect(jsonPath("$.detail")
                        .value("No applicable price found for brand 1 and product 35455 at 2020-06-14T16:00"));
    }

    @Test
    void returnsBadRequestWhenParameterIsMissing() throws Exception {
        mockMvc.perform(get(PRICES_PATH)
                        .param("brandId", "1")
                        .param("applicationDate", "2020-06-14T16:00:00"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(findApplicablePriceUseCase);
    }

    @Test
    void returnsBadRequestWhenApplicationDateIsMalformed() throws Exception {
        mockMvc.perform(get(PRICES_PATH)
                        .param("brandId", "1")
                        .param("productId", "35455")
                        .param("applicationDate", "14/06/2020 16:00"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(findApplicablePriceUseCase);
    }

    @Test
    void returnsBadRequestWhenIdentifierIsNotNumeric() throws Exception {
        mockMvc.perform(get(PRICES_PATH)
                        .param("brandId", "abc")
                        .param("productId", "35455")
                        .param("applicationDate", "2020-06-14T16:00:00"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(findApplicablePriceUseCase);
    }
}
