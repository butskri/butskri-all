package be.butskri.playground.keng.commons.test;

import be.butskri.playground.keng.commons.domain.ViewObject;
import be.butskri.playground.keng.commons.events.Event;
import be.butskri.playground.keng.commons.test.json.JsonBackwardsCompatibilityAsserter;
import be.butskri.playground.keng.commons.test.metadata.EventMetadataBackwardsCompatibilityAsserter;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.apache.commons.io.FileUtils;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.reflections.Reflections;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.stream.Collectors;

import static java.nio.charset.Charset.forName;

public abstract class AbstractBackwardsCompatibilityTest {

    private File rootFolder = new File("src/test/resources/backwardscompatibility");

    private Reflections reflections;

    @Before
    public void setUpReflections() {
        this.reflections = new Reflections(getBasePackage());
    }

    @Test
    public void eventsAreBackwardsCompatible() throws Throwable {
        assertSubclassesAreBackwardsCompatible("json/events", Event.class);
    }

    @Test
    public void viewObjectsAreBackwardsCompatible() throws Throwable {
        assertSubclassesAreBackwardsCompatible("json/view-objects", ViewObject.class);
    }

    @Test
    public void eventAnnotationsAreBackwardsCompatible() throws Throwable {
        File baseFolder = new File(rootFolder, "metadata");
        clearDirectory(new File(baseFolder, "actual"));
        Collection<Class<?>> eventsSubclasses = findAllNonAbstractSubclassesOf(Event.class);
        eventMetadataBackwardsCompatibilityAsserter().assertAnnotationsForEvents(baseFolder, eventsSubclasses);
    }

    protected abstract EnhancedRandomBuilder enhance(EnhancedRandomBuilder baseEnhancedRandomBuilder);

    protected abstract ObjectMapper getObjectMapper();

    protected abstract String getBasePackage();

    <T> void assertSubclassesAreBackwardsCompatible(String folderName, Class<T> baseClass) throws Throwable {
        Collection<Class<?>> subclasses = findAllNonAbstractSubclassesOf(baseClass);
        jsonBackwardsCompatibilityAsserter().assertJsonIsBackwardsCompatibleFor(new File(rootFolder, folderName), subclasses);
    }

    private JsonBackwardsCompatibilityAsserter jsonBackwardsCompatibilityAsserter() {
        return new JsonBackwardsCompatibilityAsserter(getObjectMapper(), randomizer());
    }

    private EventMetadataBackwardsCompatibilityAsserter eventMetadataBackwardsCompatibilityAsserter() {
        return new EventMetadataBackwardsCompatibilityAsserter(getObjectMapper());
    }

    private <T> Collection<Class<?>> findAllNonAbstractSubclassesOf(Class<T> baseClass) {
        return reflections.getSubTypesOf(baseClass)
                .stream()
                .filter(clazz -> !Modifier.isAbstract(clazz.getModifiers()))
                .collect(Collectors.toSet());
    }

    private void clearDirectory(File directory) {
        try {
            FileUtils.cleanDirectory(directory);
        } catch (IOException e) {
            Assertions.fail(String.format("Could not clean directory %s", directory), e);
        }
    }

    private EnhancedRandom randomizer() {
        EnhancedRandomBuilder baseEnhancedRandomBuilder = baseEnhancedRandomBuilder();
        return enhance(baseEnhancedRandomBuilder).build();
    }

    private EnhancedRandomBuilder baseEnhancedRandomBuilder() {
        return EnhancedRandomBuilder.aNewEnhancedRandomBuilder()
                .randomizationDepth(5)
                .charset(forName("UTF-8"))
                .stringLengthRange(5, 50)
                .collectionSizeRange(2, 2)
                .scanClasspathForConcreteTypes(true)
                .overrideDefaultInitialization(true);
    }
}
