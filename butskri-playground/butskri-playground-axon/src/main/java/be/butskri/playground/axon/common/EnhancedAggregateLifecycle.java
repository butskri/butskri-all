package be.butskri.playground.axon.common;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateLifecycle;

import java.util.*;

import static be.butskri.playground.axon.common.MatchComparators.instancesOfSameType;

public class EnhancedAggregateLifecycle {

    private PreviouslySendEvents previouslySendEvents = new PreviouslySendEvents();

    @EventSourcingHandler
    public void eventWasPublished(Event event) {
        previouslySendEvents.registerPreviousEvent(event);
    }

    public void sendEventsOnlyWhenDifferentFromPrevious(List<Event> events) {
        events.forEach(this::sendEventOnlyWhenDifferentFromPrevious);
    }

    public void sendEventOnlyWhenDifferentFromPrevious(Event event) {
        if (!previouslySendEvents.wasPreviouslySent(event)) {
            AggregateLifecycle.apply(event);
        }
    }

    public <T extends Event> AsPotentialDuplicateWhenBuilder<T> consideringEventsOfTypes(Class<? extends T>... types) {
        return matchComparator -> {
            MatchComparator<T> typesAndOthersShouldMatchComparator = TypedMatchComparator.eventsWhereTypeIn(types).and(matchComparator);
            Arrays.stream(types).forEach(type -> previouslySendEvents.configureMatchComparator(type, typesAndOthersShouldMatchComparator));
            return this;
        };
    }

    public interface AsPotentialDuplicateWhenBuilder<T extends Event> {
        EnhancedAggregateLifecycle asPotentialDuplicateWhen(MatchComparator<T> matchComparator);
    }

    private static class PreviouslySendEvents {
        private transient Map<Class, MatchComparator> matchComparatorMap = new HashMap<>();
        private List<Event> previouslySentEvents = new ArrayList<>();

        void registerPreviousEvent(Event event) {
            previouslySentEvents.add(0, event);
        }

        boolean wasPreviouslySent(Event newEvent) {
            return findPotentialDuplicateEventOf(newEvent)
                    .map(potentialDuplicateEvent -> areEqualExcludingMetadata(newEvent, potentialDuplicateEvent))
                    .orElse(false);
        }

        private Optional<Event> findPotentialDuplicateEventOf(Event event) {
            return findMostRecentEventMatching(event, matchComparatorToBeUsedFor(event));
        }

        private Optional<Event> findMostRecentEventMatching(Event newEvent, MatchComparator matchComparator) {
            return previouslySentEvents.stream()
                    .filter(event -> matchComparator.areEqual(event, newEvent))
                    .findFirst();
        }

        private MatchComparator matchComparatorToBeUsedFor(Event event) {
            MatchComparator result = matchComparatorMap.get(event.getClass());
            if (result != null) {
                return result;
            }
            return defaultMatchComparator();
        }

        private MatchComparator defaultMatchComparator() {
            return instancesOfSameType();
        }

        private boolean areEqualExcludingMetadata(Event event, Event other) {
            return EqualsBuilder.reflectionEquals(event, other, "metadata");
        }

        private void configureMatchComparator(Class<? extends Event> type, MatchComparator matchComparator) {
            matchComparatorMap.put(type, matchComparator);
        }
    }
}
