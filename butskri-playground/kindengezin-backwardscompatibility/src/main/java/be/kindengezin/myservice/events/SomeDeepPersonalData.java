package be.kindengezin.myservice.events;

import io.axoniq.gdpr.api.DeepPersonalData;
import io.axoniq.gdpr.api.PersonalData;

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
