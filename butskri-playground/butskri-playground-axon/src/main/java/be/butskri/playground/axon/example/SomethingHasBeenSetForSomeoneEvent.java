package be.butskri.playground.axon.example;

import be.butskri.playground.axon.common.Event;
import org.axonframework.modelling.command.AggregateIdentifier;

import java.util.Map;

public class SomethingHasBeenSetForSomeoneEvent extends Event {

    @AggregateIdentifier
    private MySampleAggregateId id;
    private String someone;
    private String data;

    public SomethingHasBeenSetForSomeoneEvent(Map<String, String> metadata, MySampleAggregateId id, String someone, String data) {
        super(metadata);
        this.id = id;
        this.someone = someone;
        this.data = data;
    }

    public MySampleAggregateId getId() {
        return id;
    }

    public String getSomeone() {
        return someone;
    }

    public String getData() {
        return data;
    }
}
