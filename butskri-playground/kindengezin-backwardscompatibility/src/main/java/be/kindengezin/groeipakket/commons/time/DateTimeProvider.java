package be.kindengezin.groeipakket.commons.time;

import java.time.*;
import java.time.temporal.TemporalAdjusters;

public class DateTimeProvider {
    public static final ZoneId DEFAULT_ZONE_ID = ZoneId.of("Europe/Brussels");
    private static Clock systemDefaultClock;

    public DateTimeProvider() {
    }

    public static LocalDateTime now() {
        return LocalDateTime.now(systemDefaultClock);
    }

    public static LocalDate today() {
        return LocalDate.now(systemDefaultClock);
    }

    public static LocalDate firstDayOfCurrentMonth() {
        return LocalDate.now(systemDefaultClock).with(TemporalAdjusters.firstDayOfMonth());
    }

    public static LocalDate lastDayOfCurrentMonth() {
        return LocalDate.now(systemDefaultClock).with(TemporalAdjusters.lastDayOfMonth());
    }

    public static LocalDate firstDayOfYear(Integer year) {
        return LocalDate.of(year, 1, 1);
    }

    public static LocalDate lastDayOfYear(Integer year) {
        return LocalDate.of(year, 12, 31);
    }

    public static YearMonth currentMonth() {
        return YearMonth.now(systemDefaultClock);
    }

    public static Instant timestamp() {
        return Instant.now(systemDefaultClock);
    }

    public static void setOffsetInDaysRelativeToSystemTime(int offsetInDays) {
        systemDefaultClock = Clock.offset(Clock.system(DEFAULT_ZONE_ID), Duration.ofDays((long) offsetInDays));
    }

    public static LocalDateTime toLocalDateTime(Instant instant) {
        return LocalDateTime.ofInstant(instant, DEFAULT_ZONE_ID);
    }

    static {
        systemDefaultClock = Clock.system(DEFAULT_ZONE_ID);
    }
}
