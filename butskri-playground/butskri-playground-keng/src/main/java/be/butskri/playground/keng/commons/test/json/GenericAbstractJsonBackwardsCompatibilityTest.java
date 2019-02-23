package be.butskri.playground.keng.commons.test.json;

import be.butskri.playground.keng.commons.domain.ViewObject;
import be.butskri.playground.keng.commons.events.Event;
import be.butskri.playground.keng.commons.test.AbstractJsonTest;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.Test;

import java.io.File;
import java.util.Collection;

import static java.nio.charset.Charset.forName;

public abstract class GenericAbstractJsonBackwardsCompatibilityTest extends AbstractJsonTest {

    private File resultBaseFolder = new File("src/test/resources/backwardscompatibility/json");

    @Test
    public void eventsAreBackwardsCompatible() throws Throwable {
        assertSubclassesAreBackwardsCompatible("events", Event.class);
    }

    @Test
    public void jsonIsBackwardsCompatible() throws Throwable {
        assertSubclassesAreBackwardsCompatible("view-objects", ViewObject.class);
    }

    <T> void assertSubclassesAreBackwardsCompatible(String folderName, Class<T> baseClass) throws Throwable {
        Collection<Class<?>> subclasses = findAllNonAbstractSubclassesOf(baseClass);
        jsonBackwardsCompatibilityAsserter().assertJsonIsBackwardsCompatibleFor(new File(resultBaseFolder, folderName), subclasses);
    }

    private JsonBackwardsCompatibilityAsserter jsonBackwardsCompatibilityAsserter() {
        return new JsonBackwardsCompatibilityAsserter(getObjectMapper(), randomizer());
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
