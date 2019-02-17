package be.butskri.playground.keng;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.YearMonthDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.YearMonthSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import static java.time.format.ResolverStyle.STRICT;

public class DateTimeSerializationModule extends SimpleModule {
    public static final DateTimeFormatter YEAR_MONTH_FORMAT = DateTimeFormatter.ofPattern("yyyy-M");
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE
            .withResolverStyle(STRICT);
    public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME
            .withResolverStyle(STRICT);

    public DateTimeSerializationModule() {
        addSerializer(LocalDate.class, new LocalDateSerializer(DATE_FORMAT));
        addSerializer(YearMonth.class, new YearMonthSerializer(YEAR_MONTH_FORMAT));
        addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DATE_TIME_FORMAT));
        addDeserializer(LocalDate.class, new LocalDateDeserializer(DATE_FORMAT));
        addDeserializer(YearMonth.class, new YearMonthDeserializer(YEAR_MONTH_FORMAT));
        addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DATE_TIME_FORMAT));
    }
}
