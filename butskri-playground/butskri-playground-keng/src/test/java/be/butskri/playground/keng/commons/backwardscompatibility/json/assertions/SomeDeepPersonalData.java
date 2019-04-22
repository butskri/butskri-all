package be.butskri.playground.keng.commons.backwardscompatibility.json.assertions;

import be.butskri.playground.keng.axon.PersonalData;

public class SomeDeepPersonalData {
    private String publicPart;
    @PersonalData
    private String privatePart;
}
