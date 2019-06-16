package be.butskri.playground.axon.example;

import be.butskri.playground.axon.common.AggregateEvent;
import org.axonframework.modelling.command.AggregateIdentifier;

import java.util.HashMap;

public class MySampleAggregateCreatedEvent extends AggregateEvent {

    @AggregateIdentifier
    private MySampleAggregateId id;

    public MySampleAggregateCreatedEvent(MySampleAggregateId id) {
        super(new HashMap<>());
        this.id = id;
    }

    public MySampleAggregateId getId() {
        return id;
    }
}
