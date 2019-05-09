package be.kindengezin.groeipakket.backwardscompatibility.json.assertions;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.benas.randombeans.api.EnhancedRandom;

public interface JsonBackwardsCompatibilityAsserterConfiguration {

    ObjectMapper getObjectMapper();

    EnhancedRandom getEnhancedRandom();

    boolean isFailOnMissingExpectedFileEnabled();
}
