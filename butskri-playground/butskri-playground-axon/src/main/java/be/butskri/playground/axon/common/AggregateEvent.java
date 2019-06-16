package be.butskri.playground.axon.common;

import java.util.HashMap;
import java.util.Map;

public class AggregateEvent extends EqualByStateObject {

    private Map<String, String> metadata = new HashMap<>();

    AggregateEvent() {
    }

    protected AggregateEvent(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }
}
