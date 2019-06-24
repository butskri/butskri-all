package be.butskri.playground.axon.common;

import java.util.Arrays;
import java.util.List;

public class TypedMatchComparator<T extends Event> implements MatchComparator<T> {

    private List<Class> types;

    public static <T extends Event> TypedMatchComparator<T> eventsWhereTypeIn(Class<? extends T>... types) {
        return new TypedMatchComparator<T>(Arrays.asList(types));
    }

    private TypedMatchComparator(List<Class> types) {
        this.types = types;
    }

    @Override
    public boolean areEqual(T value, T otherValue) {
        return isInstanceOfOneOfTheAllowedTypes(value) && isInstanceOfOneOfTheAllowedTypes(otherValue);
    }

    private boolean isInstanceOfOneOfTheAllowedTypes(Event value) {
        return types.stream()
                .map(clazz -> clazz.isInstance(value))
                .filter(Boolean.TRUE::equals)
                .findFirst()
                .orElse(false);
    }
}
