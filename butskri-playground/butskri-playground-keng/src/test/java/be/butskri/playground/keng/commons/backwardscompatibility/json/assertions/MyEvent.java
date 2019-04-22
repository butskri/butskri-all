package be.butskri.playground.keng.commons.backwardscompatibility.json.assertions;

import be.butskri.playground.keng.axon.DataSubjectId;
import be.butskri.playground.keng.axon.DeepPersonalData;
import be.butskri.playground.keng.axon.PersonalData;
import be.butskri.playground.keng.commons.events.Event;

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
