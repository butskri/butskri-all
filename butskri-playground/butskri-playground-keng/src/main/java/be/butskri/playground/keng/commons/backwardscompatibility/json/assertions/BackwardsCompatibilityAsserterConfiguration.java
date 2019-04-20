package be.butskri.playground.keng.commons.backwardscompatibility.json.assertions;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.benas.randombeans.api.EnhancedRandom;

public class BackwardsCompatibilityAsserterConfiguration {

    private ObjectMapper objectMapper;
    private EnhancedRandom enhancedRandom;
    private boolean failOnMissingJsonEnabled = false;

    public BackwardsCompatibilityAsserterConfiguration withFailOnMissingJsonEnabled(boolean failOnMissingJsonEnabled) {
        this.failOnMissingJsonEnabled = failOnMissingJsonEnabled;
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

    boolean isFailOnMissingJsonEnabled() {
        return failOnMissingJsonEnabled;
    }

    ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    EnhancedRandom getEnhancedRandom() {
        return enhancedRandom;
    }
}
