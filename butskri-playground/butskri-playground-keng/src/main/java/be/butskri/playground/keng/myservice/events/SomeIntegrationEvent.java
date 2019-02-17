package be.butskri.playground.keng.myservice.events;

import be.butskri.playground.keng.axon.DataSubjectId;
import be.butskri.playground.keng.axon.DeepPersonalData;
import be.butskri.playground.keng.axon.PersonalData;
import be.butskri.playground.keng.commons.annotations.CorrelationId;
import be.butskri.playground.keng.commons.events.IntegrationEvent;

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
