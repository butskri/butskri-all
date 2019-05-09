package be.kindengezin.groeipakket.backwardscompatibility.json.assertions;

import com.fasterxml.jackson.databind.ObjectMapper;

public interface MetadataBackwardsCompatibilityAsserterConfiguration {
    ObjectMapper getObjectMapper();

    boolean isFailOnMissingExpectedFile();
}
