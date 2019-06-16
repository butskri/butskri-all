package be.butskri.playground.axon.example;

import be.butskri.playground.axon.common.AggregateEvent;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.Arrays;
import java.util.List;

public class MySampleAggregateCommand {

    @TargetAggregateIdentifier
    private MySampleAggregateId id;
    private List<AggregateEvent> events;

    public MySampleAggregateCommand(MySampleAggregateId id, AggregateEvent... events) {
        this.id = id;
        this.events = Arrays.asList(events);
    }

    public MySampleAggregateId getId() {
        return id;
    }

    public List<AggregateEvent> getEvents() {
        return events;
    }
}
