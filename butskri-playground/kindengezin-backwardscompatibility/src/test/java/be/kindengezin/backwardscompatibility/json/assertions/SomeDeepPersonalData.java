package be.kindengezin.backwardscompatibility.json.assertions;

import io.axoniq.gdpr.api.PersonalData;

public class SomeDeepPersonalData {
    private String publicPart;
    @PersonalData
    private String privatePart;
}
