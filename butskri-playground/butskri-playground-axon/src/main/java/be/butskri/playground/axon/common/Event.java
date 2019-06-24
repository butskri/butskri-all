package be.butskri.playground.axon.common;

import java.util.HashMap;
import java.util.Map;

public class Event extends EqualByStateObject {

    private Map<String, String> metadata = new HashMap<>();

    Event() {
    }

    protected Event(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }
}
