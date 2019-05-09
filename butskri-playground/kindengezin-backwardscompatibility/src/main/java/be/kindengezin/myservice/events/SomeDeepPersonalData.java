package be.kindengezin.myservice.events;

import be.butskri.playground.keng.axon.DeepPersonalData;
import be.butskri.playground.keng.axon.PersonalData;

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
