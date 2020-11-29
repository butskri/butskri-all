package be.butskri.playground.images;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class ExtensiblePredicate<T> implements Predicate<T> {

    private final Class<T> type;
    private final Predicate<T> innerPredicate;

    public static <T> ExtensiblePredicate<T> objectsOfType(Class<T> type) {
        return new ExtensiblePredicate<>(type, value -> true);
    }

    public ExtensiblePredicate(Class<T> type, Predicate<T> innerPredicate) {
        this.type = type;
        this.innerPredicate = innerPredicate;
    }

    @Override
    public boolean test(T value) {
        return type.isInstance(value) && innerPredicate.test(value);
    }

    public <U extends Comparable<U>> ExtensiblePredicateBuilder<T, U> where(Function<T, U> method) {
        return new ExtensiblePredicateBuilder<>(type, innerPredicate, method);
    }

    public static class ExtensiblePredicateBuilder<T, U extends Comparable<U>> {

        private final Class<T> type;
        private final Predicate<T> innerPredicate;
        private final Function<T, U> method;

        private ExtensiblePredicateBuilder(Class<T> type, Predicate<T> innerPredicate, Function<T, U> method) {
            this.type = type;
            this.innerPredicate = innerPredicate;
            this.method = method;
        }

        public ExtensiblePredicate<T> isAfter(U comparable) {
            return createExtensiblePredicate(value -> method.apply(value).compareTo(comparable) > 0);
        }

        public ExtensiblePredicate<T> isIn(U... values) {
            List<U> valuesCollection = Arrays.asList(values);
            return createExtensiblePredicate(value -> valuesCollection.contains(method.apply(value)));
        }

        private ExtensiblePredicate<T> createExtensiblePredicate(Predicate<T> predicate) {
            return new ExtensiblePredicate<>(type, innerPredicate.and(predicate));
        }
    }
}
