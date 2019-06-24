package be.butskri.playground.axon.common;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

public class MatchComparators {

    private MatchComparators() {
    }

    static <T extends Event> MatchComparator<T> instancesOfSameType() {
        return (value, otherValue) -> value.getClass().equals(otherValue.getClass());
    }

    public static <T extends Event> MatchComparator<T> havingSameValuesFor(Function<T, Object>... functions) {
        return (value, otherValue) -> Arrays.stream(functions)
                .map(function -> Objects.equals(function.apply(value), function.apply(otherValue)))
                .reduce(true, Boolean::logicalAnd);
    }

    public static <T extends Event> MatchComparator<T> fieldsHaveSameValues(String... fields) {
        return (value, otherValue) -> havingSameValuesForFields(value, otherValue, fields);
    }

    private static boolean havingSameValuesForFields(Event value, Event otherValue, String... fields) {
        return Arrays.stream(fields)
                .map(field -> fieldHasSameValue(value, otherValue, field))
                .reduce(true, Boolean::logicalAnd);
    }

    private static boolean fieldHasSameValue(Event value, Event otherValue, String field) {
        return Objects.equals(getFieldValue(value, field), getFieldValue(otherValue, field));
    }

    private static Object getFieldValue(Object value, String fieldName) {
        try {
            Field field = getDeclaredField(value.getClass(), fieldName);
            field.setAccessible(true);
            return field.get(value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static Field getDeclaredField(Class clazz, String fieldName) {
        if (Object.class.equals(clazz)) {
            throw new RuntimeException("field " + fieldName + " cannot be found");
        }
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            return getDeclaredField(clazz.getSuperclass(), fieldName);
        }
    }
}
