package com.omarmorales.pricingapi.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Unit tests for {@link DateRange}.
 */
class DateRangeTest {

    private static final LocalDateTime START = LocalDateTime.of(2020, 6, 14, 0, 0, 0);
    private static final LocalDateTime END = LocalDateTime.of(2020, 12, 31, 23, 59, 59);

    @Nested
    @DisplayName("creation")
    class Creation {

        @Test
        void createsRangeWithGivenBoundaries() {
            DateRange range = new DateRange(START, END);

            assertThat(range.start()).isEqualTo(START);
            assertThat(range.end()).isEqualTo(END);
        }

        @Test
        void allowsSingleInstantRange() {
            DateRange range = new DateRange(START, START);

            assertThat(range.start()).isEqualTo(range.end());
        }

        @Test
        void rejectsNullStart() {
            assertThatNullPointerException()
                    .isThrownBy(() -> new DateRange(null, END))
                    .withMessage("start must not be null");
        }

        @Test
        void rejectsNullEnd() {
            assertThatNullPointerException()
                    .isThrownBy(() -> new DateRange(START, null))
                    .withMessage("end must not be null");
        }

        @Test
        void rejectsEndBeforeStart() {
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> new DateRange(END, START))
                    .withMessageContaining("must not be before start");
        }
    }

    @Nested
    @DisplayName("contains")
    class Contains {

        private final DateRange range = new DateRange(START, END);

        @Test
        void includesStartBoundary() {
            assertThat(range.contains(START)).isTrue();
        }

        @Test
        void includesEndBoundary() {
            assertThat(range.contains(END)).isTrue();
        }

        @Test
        void includesInstantInsideRange() {
            assertThat(range.contains(LocalDateTime.of(2020, 6, 14, 16, 0))).isTrue();
        }

        @ParameterizedTest
        @ValueSource(longs = {1L, 60L, 86_400L})
        void excludesInstantBeforeStart(long secondsBefore) {
            assertThat(range.contains(START.minusSeconds(secondsBefore))).isFalse();
        }

        @ParameterizedTest
        @ValueSource(longs = {1L, 60L, 86_400L})
        void excludesInstantAfterEnd(long secondsAfter) {
            assertThat(range.contains(END.plusSeconds(secondsAfter))).isFalse();
        }

        @Test
        void rejectsNullInstant() {
            assertThatNullPointerException()
                    .isThrownBy(() -> range.contains(null))
                    .withMessage("instant must not be null");
        }
    }
}
