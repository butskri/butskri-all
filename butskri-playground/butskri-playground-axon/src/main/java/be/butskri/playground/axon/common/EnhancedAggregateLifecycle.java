package be.butskri.playground.axon.common;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateLifecycle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnhancedAggregateLifecycle {

    private PreviouslySendEvents previouslySendEvents = new PreviouslySendEvents();

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

    private static class PreviouslySendEvents {
        private Map<Class, AggregateEvent> previousEvents = new HashMap<>();

        void registerPreviousEvent(AggregateEvent event) {
            previousEvents.put(event.getClass(), event);
        }

        boolean wasPreviouslySent(AggregateEvent event) {
            if (!previousEvents.containsKey(event.getClass())) {
                return false;
            }
            return areEqualExcludingMetadata(event, previousEvents.get(event.getClass()));
        }

        private boolean areEqualExcludingMetadata(AggregateEvent event, AggregateEvent other) {
            return EqualsBuilder.reflectionEquals(event, other, "metadata");
        }
    }
}
