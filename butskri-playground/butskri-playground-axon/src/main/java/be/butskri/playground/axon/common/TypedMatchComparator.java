package be.butskri.playground.axon.common;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class TypedMatchComparator<T extends AggregateEvent> implements MatchComparator<T> {

    private List<Class> types;

    public static <T extends AggregateEvent> TypedMatchComparator<T> eventsOfType(Class<T> type) {
        return new TypedMatchComparator<T>(Arrays.asList(type));
    }

    public static <T extends AggregateEvent> TypedMatchComparator<T> eventsWhereTypeIn(Class<? extends T>... types) {
        return new TypedMatchComparator<T>(Arrays.asList(types));
    }

    private TypedMatchComparator(List<Class> types) {
        this.types = types;
    }

    @Override
    public boolean areEqual(T value, T otherValue) {
        return isInstanceOfOneOfTheAllowedTypes(value) && isInstanceOfOneOfTheAllowedTypes(otherValue);
    }

    private boolean isInstanceOfOneOfTheAllowedTypes(AggregateEvent value) {
        return types.stream()
                .map(clazz -> clazz.isInstance(value))
                .filter(Boolean.TRUE::equals)
                .findFirst()
                .orElse(false);
    }

    public MatchComparator<T> withSameValuesFor(Function<T, Object>... functions) {
        return and(havingSameValuesFor(functions));
    }

    public static <T extends AggregateEvent> MatchComparator<T> havingSameValuesFor(Function<T, Object>... functions) {
        return (value, otherValue) -> Arrays.stream(functions)
                .map(function -> Objects.equals(function.apply(value), function.apply(otherValue)))
                .reduce(true, Boolean::logicalAnd);
    }

    public MatchComparator<T> withAllFieldsHavingSameValue(String... fields) {
        return and(fieldsHaveSameValues(fields));
    }

    public static <T extends AggregateEvent> MatchComparator<T> fieldsHaveSameValues(String... fields) {
        return (value, otherValue) -> havingSameValuesForFields(value, otherValue, fields);
    }

    public static boolean havingSameValuesForFields(AggregateEvent value, AggregateEvent otherValue, String... fields) {
        return Arrays.stream(fields)
                .map(field -> fieldHasSameValue(value, otherValue, field))
                .reduce(true, Boolean::logicalAnd);
    }

    private static boolean fieldHasSameValue(AggregateEvent value, AggregateEvent otherValue, String field) {
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
