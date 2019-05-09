package be.kindengezin.groeipakket.backwardscompatibility.json;

import be.kindengezin.groeipakket.backwardscompatibility.json.assertions.JsonBackwardsCompatibilityAsserter;
import be.kindengezin.groeipakket.backwardscompatibility.json.assertions.MetadataBackwardsCompatibilityAsserter;
import be.kindengezin.groeipakket.backwardscompatibility.json.random.RandomizationTestConstants;
import be.kindengezin.groeipakket.backwardscompatibility.json.reflection.ClassFinder;
import be.kindengezin.groeipakket.commons.domain.event.Event;
import be.kindengezin.groeipakket.domain.read.ViewObject;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import org.axonframework.spring.stereotype.Saga;
import org.junit.Test;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.Collection;

public abstract class AbstractJsonBackwardsCompatibilityTest {

    private static final File DEFAULT_ROOT_FOLDER = new File("src/test/resources/backwardscompatibility");
    private static final String DEFAULT_BASE_PACKAGE = "be.kindengezin";

    private JsonBackwardsCompatibilityTestConfiguration cachedTestConfiguration;

    @Test
    public void sagasAreBackwardsCompatible() throws Throwable {
        assertAnnotatedClassesAreBackwardsCompatible("sagas/json", Saga.class);
    }

    @Test
    public void eventsAreBackwardsCompatible() throws Throwable {
        assertSubclassesAreBackwardsCompatible("events/json", Event.class);
    }

    @Test
    public void viewObjectsAreBackwardsCompatible() throws Throwable {
        assertSubclassesAreBackwardsCompatible("view-objects/json", ViewObject.class);
    }

    @Test
    public void eventMetadataIsBackwardsCompatible() throws Throwable {
        File baseFolder = folder("events/metadata");
        metadataBackwardsCompatibilityAsserter().assertAnnotationsForEvents(baseFolder, classFinder().findAllNonAbstractSubclassesOf(Event.class));
    }

    @Test
    public void deepPersonalDataMetadataIsBackwardsCompatible() throws Throwable {
        File baseFolder = folder("deeppersonaldata/metadata");
        metadataBackwardsCompatibilityAsserter()
                .assertGdprAnnotations(baseFolder, cachedTestConfiguration().getDeepPersonalDataClasses());
    }

    protected JsonBackwardsCompatibilityTestConfiguration testConfiguration() {
        return new JsonBackwardsCompatibilityTestConfiguration()
                .withBasePackage(DEFAULT_BASE_PACKAGE)
                .withRootFolder(DEFAULT_ROOT_FOLDER)
                .withFailOnMissingExpectedFileEnabled(false)
                .withEnhancedRandom(enhancedRandomBuilder().build());
    }

    protected EnhancedRandomBuilder enhancedRandomBuilder() {
        return RandomizationTestConstants.baseEnhancedRandomBuilder();
    }

    <T> void assertSubclassesAreBackwardsCompatible(String folderName, Class<T> baseClass) throws Throwable {
        Collection<Class<?>> subclasses = classFinder().findAllNonAbstractSubclassesOf(baseClass);
        jsonBackwardsCompatibilityAsserter().assertJsonIsBackwardsCompatibleFor(folder(folderName), subclasses);
    }

    <T extends Annotation> void assertAnnotatedClassesAreBackwardsCompatible(String folderName, Class<T> baseAnnotation) throws Throwable {
        Collection<Class<?>> subclasses = classFinder().findAllNonAbstractClassesAnnotatedWith(baseAnnotation);
        jsonBackwardsCompatibilityAsserter().assertJsonIsBackwardsCompatibleFor(folder(folderName), subclasses);
    }

    private JsonBackwardsCompatibilityTestConfiguration cachedTestConfiguration() {
        if (this.cachedTestConfiguration == null) {
            this.cachedTestConfiguration = testConfiguration();
        }
        return cachedTestConfiguration;
    }

    private JsonBackwardsCompatibilityAsserter jsonBackwardsCompatibilityAsserter() {
        return new JsonBackwardsCompatibilityAsserter(cachedTestConfiguration());
    }

    private MetadataBackwardsCompatibilityAsserter metadataBackwardsCompatibilityAsserter() {
        return new MetadataBackwardsCompatibilityAsserter(cachedTestConfiguration());
    }

    private ClassFinder classFinder() {
        return cachedTestConfiguration().getClassFinder();
    }

    private File folder(String folderName) {
        return new File(cachedTestConfiguration().getRootFolder(), folderName);
    }
}
