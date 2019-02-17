package be.butskri.playground.axon.events;

import be.butskri.playground.axon.annotations.DeepPersonalData;
import be.butskri.playground.axon.annotations.PersonalData;

public class SomeDeepPersonalData {

    private String publicPart;
    @PersonalData
    private String privatePart;
    @DeepPersonalData
    private SomeDeeperPersonalData deeperPersonalData;

    public String getPublicPart() {
        return publicPart;
    }

    public String getPrivatePart() {
        return privatePart;
    }

    public SomeDeeperPersonalData getDeeperPersonalData() {
        return deeperPersonalData;
    }
}
