package be.butskri.playground.axon.events;

import be.butskri.playground.axon.annotations.CorrelationId;
import be.butskri.playground.axon.annotations.DataSubjectId;
import be.butskri.playground.axon.annotations.DeepPersonalData;
import be.butskri.playground.axon.annotations.PersonalData;

import java.util.UUID;

public class SomeIntegrationEvent extends IntegrationEvent {

    @CorrelationId
    @DataSubjectId
    private UUID myId;

    @PersonalData
    private String somePersonalData;
    private String someNonPersonalData;
    @PersonalData
    private UUID someOtherPersonalData;
    @DeepPersonalData
    private SomeDeepPersonalData deepPersonalData;

    public UUID getMyId() {
        return myId;
    }

    public String getSomePersonalData() {
        return somePersonalData;
    }

    public String getSomeNonPersonalData() {
        return someNonPersonalData;
    }

    public UUID getSomeOtherPersonalData() {
        return someOtherPersonalData;
    }

    public SomeDeepPersonalData getDeepPersonalData() {
        return deepPersonalData;
    }
}
