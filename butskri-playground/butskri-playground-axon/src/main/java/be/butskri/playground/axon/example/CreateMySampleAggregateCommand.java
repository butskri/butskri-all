package be.butskri.playground.axon.example;

import be.butskri.playground.axon.common.Event;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.Arrays;
import java.util.List;

public class CreateMySampleAggregateCommand {

    @TargetAggregateIdentifier
    private MySampleAggregateId id;
    private List<Event> events;

    public CreateMySampleAggregateCommand(MySampleAggregateId id, Event... events) {
        this.id = id;
        this.events = Arrays.asList(events);
    }

    public MySampleAggregateId getId() {
        return id;
    }

    public List<Event> getEvents() {
        return events;
    }
}
