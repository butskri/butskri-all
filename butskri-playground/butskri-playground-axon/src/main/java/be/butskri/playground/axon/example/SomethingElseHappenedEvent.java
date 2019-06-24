package be.butskri.playground.axon.example;

import be.butskri.playground.axon.common.Event;
import org.axonframework.modelling.command.AggregateIdentifier;

import java.util.Map;

public class SomethingElseHappenedEvent extends Event {

    @AggregateIdentifier
    private MySampleAggregateId id;
    private String data;

    public SomethingElseHappenedEvent(Map<String, String> metadata, MySampleAggregateId id, String data) {
        super(metadata);
        this.id = id;
        this.data = data;
    }

    public MySampleAggregateId getId() {
        return id;
    }

    public String getData() {
        return data;
    }
}
