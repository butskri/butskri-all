package be.butskri.playground.axon.example;

import be.butskri.playground.axon.common.EqualByStateObject;

import java.util.UUID;

public class MySampleAggregateId extends EqualByStateObject {

    private UUID value;

    public MySampleAggregateId(UUID value) {
        this.value = value;
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
