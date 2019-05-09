package be.kindengezin.myservice.events;

import io.axoniq.gdpr.api.DataSubjectId;
import io.axoniq.gdpr.api.DeepPersonalData;
import io.axoniq.gdpr.api.PersonalData;
import be.kindengezin.groeipakket.commons.domain.event.Event;

import java.util.UUID;

public class SomeEvent extends Event {

    @DataSubjectId
    private UUID myId;

    private SomeId someId;
    private SomeOtherId someOtherId;
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

    public SomeId getSomeId() {
        return someId;
    }

    public SomeOtherId getSomeOtherId() {
        return someOtherId;
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
