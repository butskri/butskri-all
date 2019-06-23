package be.butskri.playground.axon.common;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateLifecycle;

import java.util.*;

public class EnhancedAggregateLifecycle {

    private transient PreviouslySendEvents previouslySendEvents = new PreviouslySendEvents();

    @EventSourcingHandler
    public void eventWasPublished(AggregateEvent event) {
        previouslySendEvents.registerPreviousEvent(event);
    }

    public void sendEventsOnlyWhenDifferentFromPrevious(List<AggregateEvent> events) {
        events.forEach(this::sendEventOnlyWhenDifferentFromPrevious);
    }

    public void sendEventOnlyWhenDifferentFromPrevious(AggregateEvent event) {
        if (!previouslySendEvents.wasPreviouslySent(event)) {
            AggregateLifecycle.apply(event);
        }
    }

    public AsPotentialDuplicatesForBuilder considering(MatchComparator matchComparator) {
        return types -> {
            Arrays.stream(types).forEach(type -> previouslySendEvents.useMatchComparator(type, matchComparator));
            return this;
        };
    }

    public interface AsPotentialDuplicatesForBuilder {
        EnhancedAggregateLifecycle asPotentialDuplicateFor(Class<? extends AggregateEvent>... types);
    }

    private static class PreviouslySendEvents {
        private Map<Class, MatchComparator> matchComparatorMap = new HashMap<>();
        private List<AggregateEvent> previouslySentEvents = new ArrayList<>();

        void registerPreviousEvent(AggregateEvent event) {
            previouslySentEvents.add(0, event);
        }

        boolean wasPreviouslySent(AggregateEvent newEvent) {
            return findPotentialDuplicateEventOf(newEvent)
                    .map(potentialDuplicateEvent -> areEqualExcludingMetadata(newEvent, potentialDuplicateEvent))
                    .orElse(false);
        }

        private Optional<AggregateEvent> findPotentialDuplicateEventOf(AggregateEvent event) {
            return findMostRecentEventMatching(event, matchComparatorToBeUsedFor(event));
        }

        private Optional<AggregateEvent> findMostRecentEventMatching(AggregateEvent newEvent, MatchComparator matchComparator) {
            return previouslySentEvents.stream()
                    .filter(event -> matchComparator.areEqual(event, newEvent))
                    .findFirst();
        }

        private MatchComparator matchComparatorToBeUsedFor(AggregateEvent event) {
            MatchComparator result = matchComparatorMap.get(event.getClass());
            if (result != null) {
                return result;
            }
            return defaultMatchComparator();
        }

        private MatchComparator defaultMatchComparator() {
            return (value, otherValue) -> value.getClass().equals(otherValue.getClass());
        }

        private boolean areEqualExcludingMetadata(AggregateEvent event, AggregateEvent other) {
            return EqualsBuilder.reflectionEquals(event, other, "metadata");
        }

        private void useMatchComparator(Class<? extends AggregateEvent> type, MatchComparator matchComparator) {
            matchComparatorMap.put(type, matchComparator);
        }
    }
}
