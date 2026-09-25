package com.omarmorales.pricingapi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.jayway.jsonpath.JsonPath;

import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.json.JsonMapper;

/**
 * End-to-end tests of the applicable-price endpoint against the seed data in {@code data.sql}.
 *
 * <p>Each case matches a request of the {@code pricingapi_tests.json} Postman collection and prints
 * the request and the response to the console.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class FindApplicablePriceIntegrationTest {

    private static final String PRICES_PATH = "/api/v1/prices";
    private static final String SEPARATOR = "=".repeat(70);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    @ParameterizedTest(name = "Test {index}: {0} -> rate {1}, price {2}", quoteTextArguments = false)
    @CsvSource({
            "2020-06-14T10:00:00, 1, 35.50, 2020-06-14T00:00:00, 2020-12-31T23:59:59",
            "2020-06-14T16:00:00, 2, 25.45, 2020-06-14T15:00:00, 2020-06-14T18:30:00",
            "2020-06-14T21:00:00, 1, 35.50, 2020-06-14T00:00:00, 2020-12-31T23:59:59",
            "2020-06-15T10:00:00, 3, 30.50, 2020-06-15T00:00:00, 2020-06-15T11:00:00",
            "2020-06-16T21:00:00, 4, 38.95, 2020-06-15T16:00:00, 2020-12-31T23:59:59"
    })
    void returnsApplicablePrice(String applicationDate, long applicableRate, double price,
                                String startDate, String endDate, TestInfo testInfo) throws Exception {
        MvcResult result = mockMvc.perform(get(PRICES_PATH)
                        .queryParam("brandId", "1")
                        .queryParam("productId", "35455")
                        .queryParam("applicationDate", applicationDate))
                .andDo(exchange -> printExchange(testInfo.getDisplayName(), exchange))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.productId").value(35455))
                .andExpect(jsonPath("$.brandId").value(1))
                .andExpect(jsonPath("$.applicableRate").value(applicableRate))
                .andExpect(jsonPath("$.startDate").value(startDate))
                .andExpect(jsonPath("$.endDate").value(endDate))
                .andExpect(jsonPath("$.price").value(price))
                .andExpect(jsonPath("$.currency").value("EUR"))
                .andReturn();

        // The application date must fall within the returned [startDate, endDate] range
        String body = result.getResponse().getContentAsString();
        LocalDateTime start = LocalDateTime.parse(JsonPath.read(body, "$.startDate"));
        LocalDateTime end = LocalDateTime.parse(JsonPath.read(body, "$.endDate"));
        assertThat(LocalDateTime.parse(applicationDate)).isBetween(start, end);
    }

    /**
     * Prints the request and the pretty-printed JSON response. Runs before the assertions so the
     * exchange is shown even when a case fails.
     */
    private void printExchange(String testName, MvcResult exchange) throws Exception {
        MockHttpServletRequest request = exchange.getRequest();
        MockHttpServletResponse response = exchange.getResponse();
        String body = response.getContentAsString();
        String prettyBody = body.isBlank()
                ? "<empty>"
                : jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                        // Keep decimals as sent (35.50, not 35.5)
                        jsonMapper.reader().with(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS).readTree(body));

        System.out.printf("""

                %1$s
                 %2$s
                %1$s
                 REQUEST
                   %3$s %4$s?%5$s

                 RESPONSE
                   Status: %6$d
                %7$s
                %1$s
                """,
                SEPARATOR, testName,
                request.getMethod(), request.getRequestURI(), request.getQueryString(),
                response.getStatus(), prettyBody.indent(3).stripTrailing());
    }
}
