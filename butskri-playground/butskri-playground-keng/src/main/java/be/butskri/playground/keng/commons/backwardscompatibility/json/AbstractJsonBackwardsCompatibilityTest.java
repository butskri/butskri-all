package be.butskri.playground.keng.commons.backwardscompatibility.json;

import be.butskri.playground.keng.commons.backwardscompatibility.json.assertions.JsonBackwardsCompatibilityAsserter;
import be.butskri.playground.keng.commons.backwardscompatibility.json.metadata.EventMetadataBackwardsCompatibilityAsserter;
import be.butskri.playground.keng.commons.domain.ProcessManager;
import be.butskri.playground.keng.commons.domain.ViewObject;
import be.butskri.playground.keng.commons.events.Event;
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

public abstract class AbstractJsonBackwardsCompatibilityTest {

    private static final int DEFAULT_MIN_STRING_LENGTH = 5;
    private static final int DEFAULT_MAX_STRING_LENGTH = 50;
    private static final int DEFAULT_MIN_COLLECTION_SIZE = 2;
    private static final int DEFAULT_MAX_COLLECTION_SIZE = 2;
    private static final int RANDOMIZATION_DEPTH = 5;
    private File DEFAULT_ROOT_FOLDER = new File("src/test/resources/backwardscompatibility");

    private Reflections reflections;

    @Before
    public void setUpReflections() {
        this.reflections = new Reflections(getBasePackage());
    }

    @Test
    public void sagasAreBackwardsCompatible() throws Throwable {
        assertSubclassesAreBackwardsCompatible("json/sagas", ProcessManager.class);
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
        File baseFolder = new File(getRootFolder(), "metadata");
        clearDirectory(new File(baseFolder, "actual"));
        Collection<Class<?>> eventsSubclasses = findAllNonAbstractSubclassesOf(Event.class);
        eventMetadataBackwardsCompatibilityAsserter().assertAnnotationsForEvents(baseFolder, eventsSubclasses);
    }

    protected File getRootFolder() {
        return DEFAULT_ROOT_FOLDER;
    }

    protected abstract ObjectMapper getObjectMapper();

    protected abstract String getBasePackage();

    <T> void assertSubclassesAreBackwardsCompatible(String folderName, Class<T> baseClass) throws Throwable {
        Collection<Class<?>> subclasses = findAllNonAbstractSubclassesOf(baseClass);
        jsonBackwardsCompatibilityAsserter().assertJsonIsBackwardsCompatibleFor(new File(getRootFolder(), folderName), subclasses);
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
        return enhancedRandomBuilder().build();
    }

    protected EnhancedRandomBuilder enhancedRandomBuilder() {
        return EnhancedRandomBuilder.aNewEnhancedRandomBuilder()
                .randomizationDepth(RANDOMIZATION_DEPTH)
                .charset(forName("UTF-8"))
                .stringLengthRange(DEFAULT_MIN_STRING_LENGTH, DEFAULT_MAX_STRING_LENGTH)
                .collectionSizeRange(DEFAULT_MIN_COLLECTION_SIZE, DEFAULT_MAX_COLLECTION_SIZE)
                .scanClasspathForConcreteTypes(true)
                .overrideDefaultInitialization(true);
    }
}
