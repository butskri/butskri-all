package be.kindengezin.backwardscompatibility.json.assertions;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.benas.randombeans.api.EnhancedRandom;

public class BackwardsCompatibilityAsserterConfiguration {

    private ObjectMapper objectMapper;
    private EnhancedRandom enhancedRandom;
    private boolean failOnMissingExpectedFile = false;

    public BackwardsCompatibilityAsserterConfiguration withFailOnMissingExpectedFile(boolean failOnMissingExpectedFile) {
        this.failOnMissingExpectedFile = failOnMissingExpectedFile;
        return this;
    }

    public BackwardsCompatibilityAsserterConfiguration withObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        return this;
    }

    public BackwardsCompatibilityAsserterConfiguration withEnhancedRandom(EnhancedRandom enhancedRandom) {
        this.enhancedRandom = enhancedRandom;
        return this;
    }

    boolean isFailOnMissingExpectedFile() {
        return failOnMissingExpectedFile;
    }

    ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    EnhancedRandom getEnhancedRandom() {
        return enhancedRandom;
    }
}
