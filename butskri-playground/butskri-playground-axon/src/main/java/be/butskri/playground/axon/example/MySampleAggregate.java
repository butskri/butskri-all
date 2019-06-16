package be.butskri.playground.axon.example;

import be.butskri.playground.axon.common.EnhancedAggregateLifecycle;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateMember;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class MySampleAggregate {

    @AggregateIdentifier
    private MySampleAggregateId id;
    @AggregateMember
    private EnhancedAggregateLifecycle lifecycle = new EnhancedAggregateLifecycle();

    private MySampleAggregate() {
    }

    @CommandHandler
    public MySampleAggregate(CreateMySampleAggregateCommand createCommand) {
        this.id = createCommand.getId();
        lifecycle.sendEventOnlyWhenDifferentFromPrevious(new MySampleAggregateCreatedEvent(createCommand.getId()));
        lifecycle.sendEventsOnlyWhenDifferentFromPrevious(createCommand.getEvents());
    }

    @CommandHandler
    public void execute(MySampleAggregateCommand command) {
        lifecycle.sendEventsOnlyWhenDifferentFromPrevious(command.getEvents());
    }

    @EventSourcingHandler
    public void on(MySampleAggregateCreatedEvent createdEvent) {
        this.id = createdEvent.getId();
    }
}
