package be.butskri.playground.axon.common;

import java.util.function.Predicate;

public interface MatchComparator<T extends Event> {

    boolean areEqual(T value, T otherValue);

    default Predicate<T> matchingValue(T value) {
        return (otherValue) -> areEqual(value, otherValue);
    }

    default MatchComparator<T> or(MatchComparator<T> other) {
        return (value, otherValue) -> this.areEqual(value, otherValue) || other.areEqual(value, otherValue);
    }

    default MatchComparator<T> and(MatchComparator<T> other) {
        return (value, otherValue) -> this.areEqual(value, otherValue) && other.areEqual(value, otherValue);
    }
}
