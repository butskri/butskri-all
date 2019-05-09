package be.kindengezin.groeipakket.backwardscompatibility.json;

import be.kindengezin.groeipakket.backwardscompatibility.json.assertions.JsonBackwardsCompatibilityAsserterConfiguration;
import be.kindengezin.groeipakket.backwardscompatibility.json.assertions.MetadataBackwardsCompatibilityAsserterConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.benas.randombeans.api.EnhancedRandom;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

public class JsonBackwardsCompatibilityTestConfiguration
        implements JsonBackwardsCompatibilityAsserterConfiguration, MetadataBackwardsCompatibilityAsserterConfiguration {

    private String basePackage;
    private File rootFolder;
    private ObjectMapper objectMapper;
    private EnhancedRandom enhancedRandom;
    private boolean failOnMissingExpectedFile = false;
    private Collection<Class<?>> deepPersonalDataClasses = new ArrayList<>();

    public JsonBackwardsCompatibilityTestConfiguration withBasePackage(String basePackage) {
        this.basePackage = basePackage;
        return this;
    }

    public JsonBackwardsCompatibilityTestConfiguration withRootFolder(File rootFolder) {
        this.rootFolder = rootFolder;
        return this;
    }

    public JsonBackwardsCompatibilityTestConfiguration withFailOnMissingExpectedFile(boolean failOnMissingExpectedFile) {
        this.failOnMissingExpectedFile = failOnMissingExpectedFile;
        return this;
    }

    public JsonBackwardsCompatibilityTestConfiguration withObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        return this;
    }

    public JsonBackwardsCompatibilityTestConfiguration withEnhancedRandom(EnhancedRandom enhancedRandom) {
        this.enhancedRandom = enhancedRandom;
        return this;
    }

    public JsonBackwardsCompatibilityTestConfiguration withDeepPersonalDataClasses(Collection<Class<?>> deepPersonalDataClasses) {
        this.deepPersonalDataClasses = deepPersonalDataClasses;
        return this;
    }

    String getBasePackage() {
        return basePackage;
    }

    File getRootFolder() {
        return rootFolder;
    }

    @Override
    public boolean isFailOnMissingExpectedFile() {
        return failOnMissingExpectedFile;
    }

    @Override
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    @Override
    public EnhancedRandom getEnhancedRandom() {
        return enhancedRandom;
    }

    public Collection<Class<?>> getDeepPersonalDataClasses() {
        return deepPersonalDataClasses;
    }
}
