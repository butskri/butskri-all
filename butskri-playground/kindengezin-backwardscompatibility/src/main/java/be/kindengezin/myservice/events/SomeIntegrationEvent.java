package be.kindengezin.myservice.events;

import io.axoniq.gdpr.api.DataSubjectId;
import io.axoniq.gdpr.api.DeepPersonalData;
import io.axoniq.gdpr.api.PersonalData;
import be.kindengezin.groeipakket.commons.integration.annotations.producer.CorrelationId;
import be.kindengezin.groeipakket.commons.domain.event.IntegrationEvent;

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
