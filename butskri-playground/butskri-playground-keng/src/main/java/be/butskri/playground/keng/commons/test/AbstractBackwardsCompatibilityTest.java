package be.butskri.playground.keng.commons.test;

import be.butskri.playground.keng.commons.domain.ViewObject;
import be.butskri.playground.keng.commons.events.Event;
import be.butskri.playground.keng.commons.test.json.JsonBackwardsCompatibilityAsserter;
import be.butskri.playground.keng.commons.test.metadata.EventMetadataBackwardsCompatibilityAsserter;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.apache.commons.io.FileUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import static java.nio.charset.Charset.forName;

public abstract class AbstractBackwardsCompatibilityTest extends AbstractJsonTest {

    private File baseFolder = new File("src/test/resources/backwardscompatibility");

    @Test
    public void eventsAreBackwardsCompatible() throws Throwable {
        assertSubclassesAreBackwardsCompatible("json/events", Event.class);
    }

    @Test
    public void jsonIsBackwardsCompatible() throws Throwable {
        assertSubclassesAreBackwardsCompatible("json/view-objects", ViewObject.class);
    }

    @Test
    public void eventAnnotationsShouldBeBackwardsCompatible() {
        File resultBaseFolder = new File(baseFolder, "metadata");
        clearDirectory(new File(resultBaseFolder, "actual"));
        Collection<Class<?>> eventsToBeChecked = findAllNonAbstractSubclassesOf(Event.class);
        eventMetadataBackwardsCompatibilityAsserter().assertAnnotationsForEvents(resultBaseFolder, eventsToBeChecked);
    }

    <T> void assertSubclassesAreBackwardsCompatible(String folderName, Class<T> baseClass) throws Throwable {
        Collection<Class<?>> subclasses = findAllNonAbstractSubclassesOf(baseClass);
        jsonBackwardsCompatibilityAsserter().assertJsonIsBackwardsCompatibleFor(new File(baseFolder, folderName), subclasses);
    }

    private JsonBackwardsCompatibilityAsserter jsonBackwardsCompatibilityAsserter() {
        return new JsonBackwardsCompatibilityAsserter(getObjectMapper(), randomizer());
    }

    private EventMetadataBackwardsCompatibilityAsserter eventMetadataBackwardsCompatibilityAsserter() {
        return new EventMetadataBackwardsCompatibilityAsserter(getObjectMapper());
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

    protected abstract EnhancedRandomBuilder enhance(EnhancedRandomBuilder baseEnhancedRandomBuilder);

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
