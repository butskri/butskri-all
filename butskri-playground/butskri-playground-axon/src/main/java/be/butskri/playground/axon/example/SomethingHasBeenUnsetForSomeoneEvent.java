package be.butskri.playground.axon.example;

import be.butskri.playground.axon.common.AggregateEvent;
import org.axonframework.modelling.command.AggregateIdentifier;

import java.util.Map;

public class SomethingHasBeenUnsetForSomeoneEvent extends AggregateEvent {

    @AggregateIdentifier
    private MySampleAggregateId id;
    private String someone;

    public SomethingHasBeenUnsetForSomeoneEvent(Map<String, String> metadata, MySampleAggregateId id, String someone) {
        super(metadata);
        this.id = id;
        this.someone = someone;
    }

    public MySampleAggregateId getId() {
        return id;
    }

    public String getSomeone() {
        return someone;
    }

}
