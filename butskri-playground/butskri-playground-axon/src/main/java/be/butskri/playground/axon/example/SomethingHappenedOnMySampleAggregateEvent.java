package be.butskri.playground.axon.example;

import be.butskri.playground.axon.common.AggregateEvent;
import org.axonframework.modelling.command.AggregateIdentifier;

import java.util.Map;

public class SomethingHappenedOnMySampleAggregateEvent extends AggregateEvent {

    @AggregateIdentifier
    private MySampleAggregateId id;
    private String data;

    public SomethingHappenedOnMySampleAggregateEvent(Map<String, String> metadata, MySampleAggregateId id, String data) {
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
