package be.kindengezin.backwardscompatibility.json.assertions;

import io.axoniq.gdpr.api.DataSubjectId;
import io.axoniq.gdpr.api.DeepPersonalData;
import io.axoniq.gdpr.api.PersonalData;
import be.kindengezin.groeipakket.commons.domain.event.Event;

import java.util.UUID;

public class MyEvent extends Event {

    @DataSubjectId
    private UUID id;
    private String myString;
    private int myInt;
    @PersonalData
    private String personalData;
    @DeepPersonalData
    private SomeDeepPersonalData deepPersonalData;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMyString() {
        return myString;
    }

    public void setMyString(String myString) {
        this.myString = myString;
    }

    public int getMyInt() {
        return myInt;
    }

    public void setMyInt(int myInt) {
        this.myInt = myInt;
    }

    public String getPersonalData() {
        return personalData;
    }

    public void setPersonalData(String personalData) {
        this.personalData = personalData;
    }

    public SomeDeepPersonalData getDeepPersonalData() {
        return deepPersonalData;
    }

    public void setDeepPersonalData(SomeDeepPersonalData deepPersonalData) {
        this.deepPersonalData = deepPersonalData;
    }
}
