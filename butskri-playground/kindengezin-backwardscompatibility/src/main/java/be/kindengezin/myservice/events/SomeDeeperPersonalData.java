package be.kindengezin.myservice.events;

import io.axoniq.gdpr.api.DeepPersonalData;
import io.axoniq.gdpr.api.PersonalData;

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
