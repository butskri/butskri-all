package be.kindengezin.backwardscompatibility.json.assertions;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.benas.randombeans.api.EnhancedRandom;

import java.util.ArrayList;
import java.util.Collection;

public class JsonBackwardsCompatibilityAsserterConfiguration {

    private ObjectMapper objectMapper;
    private EnhancedRandom enhancedRandom;
    private boolean failOnMissingExpectedFile = false;
    private Collection<Class<?>> deepPersonalDataClasses = new ArrayList<>();

    public JsonBackwardsCompatibilityAsserterConfiguration withFailOnMissingExpectedFile(boolean failOnMissingExpectedFile) {
        this.failOnMissingExpectedFile = failOnMissingExpectedFile;
        return this;
    }

    public JsonBackwardsCompatibilityAsserterConfiguration withObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        return this;
    }

    public JsonBackwardsCompatibilityAsserterConfiguration withEnhancedRandom(EnhancedRandom enhancedRandom) {
        this.enhancedRandom = enhancedRandom;
        return this;
    }

    public JsonBackwardsCompatibilityAsserterConfiguration withDeepPersonalDataClasses(Collection<Class<?>> deepPersonalDataClasses) {
        this.deepPersonalDataClasses = deepPersonalDataClasses;
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

    public Collection<Class<?>> getDeepPersonalDataClasses() {
        return deepPersonalDataClasses;
    }
}
