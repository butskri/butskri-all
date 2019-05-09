package be.kindengezin.myservice.events;

import io.axoniq.gdpr.api.PersonalData;

public class SomeDeepestPersonalData {

    private String publicPart;
    @PersonalData
    private String privatePart;
    @PersonalData
    private int anotherPrivatePart;

    public String getPublicPart() {
        return publicPart;
    }

    public String getPrivatePart() {
        return privatePart;
    }

    public int getAnotherPrivatePart() {
        return anotherPrivatePart;
    }
}
