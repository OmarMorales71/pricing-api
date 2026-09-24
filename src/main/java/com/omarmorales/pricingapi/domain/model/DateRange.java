package com.omarmorales.pricingapi.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Closed time interval during which something is valid. Both boundaries are inclusive.
 *
 * @param start the first instant of the range, inclusive
 * @param end   the last instant of the range, inclusive
 */
public record DateRange(LocalDateTime start, LocalDateTime end) {

    /**
     * Creates a date range.
     *
     * @throws NullPointerException     if {@code start} or {@code end} is {@code null}
     * @throws IllegalArgumentException if {@code end} is before {@code start}
     */
    public DateRange {
        Objects.requireNonNull(start, "start must not be null");
        Objects.requireNonNull(end, "end must not be null");
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("end (%s) must not be before start (%s)".formatted(end, start));
        }
    }

    /**
     * Checks whether the given instant falls within this range, boundaries included.
     *
     * @param instant the instant to check
     * @return {@code true} if {@code instant} is between {@code start} and {@code end}, both inclusive
     * @throws NullPointerException if {@code instant} is {@code null}
     */
    public boolean contains(LocalDateTime instant) {
        Objects.requireNonNull(instant, "instant must not be null");
        return !instant.isBefore(start) && !instant.isAfter(end);
    }
}
