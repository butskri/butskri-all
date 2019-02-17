package be.butskri.playground.axon.events;

import be.butskri.playground.axon.annotations.DeepPersonalData;
import be.butskri.playground.axon.annotations.PersonalData;

public class SomeDeeperPersonalData {

    private String publicPart;
    @PersonalData
    private String privatePart;
    @DeepPersonalData
    private SomeDeepestPersonalData deepestPersonalData;

    public String getPublicPart() {
        return publicPart;
    }

    public String getPrivatePart() {
        return privatePart;
    }

    public SomeDeepestPersonalData getDeepestPersonalData() {
        return deepestPersonalData;
    }
}
