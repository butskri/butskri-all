package be.kindengezin.groeipakket.backwardscompatibility.json.assertions;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collection;

public interface MetadataBackwardsCompatibilityAsserterConfiguration {
    ObjectMapper getObjectMapper();

    boolean isFailOnMissingExpectedFileEnabled();

    Collection<Class<?>> getDeepPersonalDataClasses();

}
