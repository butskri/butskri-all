package be.kindengezin.groeipakket.backwardscompatibility.json;

import be.kindengezin.groeipakket.backwardscompatibility.json.assertions.JsonBackwardsCompatibilityAsserter;
import be.kindengezin.groeipakket.backwardscompatibility.json.assertions.MetadataBackwardsCompatibilityAsserter;
import be.kindengezin.groeipakket.backwardscompatibility.json.random.RandomizationTestConstants;
import be.kindengezin.groeipakket.commons.domain.event.Event;
import be.kindengezin.groeipakket.domain.read.ViewObject;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import org.axonframework.spring.stereotype.Saga;
import org.junit.Before;
import org.junit.Test;
import org.reflections.Reflections;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class AbstractJsonBackwardsCompatibilityTest {

    private static final File DEFAULT_ROOT_FOLDER = new File("src/test/resources/backwardscompatibility");
    private static final String DEFAULT_BASE_PACKAGE = "be.kindengezin";

    private Reflections reflections;
    private JsonBackwardsCompatibilityTestConfiguration cachedTestConfiguration;

    @Before
    public void setUpReflections() {
        this.reflections = new Reflections(cachedTestConfiguration().getBasePackage());
    }

    @Test
    public void sagasAreBackwardsCompatible() throws Throwable {
        assertAnnotatedClassesAreBackwardsCompatible("json/sagas", Saga.class);
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
    public void eventMetadataIsBackwardsCompatible() throws Throwable {
        File baseFolder = folder("metadata/events");
        metadataBackwardsCompatibilityAsserter().assertAnnotationsForEvents(baseFolder, findAllNonAbstractSubclassesOf(Event.class));
    }

    @Test
    public void deepPersonalDataMetadataIsBackwardsCompatible() throws Throwable {
        File baseFolder = folder("metadata/deeppersonaldata");
        metadataBackwardsCompatibilityAsserter()
                .assertGdprAnnotations(baseFolder, cachedTestConfiguration().getDeepPersonalDataClasses());
    }

    protected JsonBackwardsCompatibilityTestConfiguration testConfiguration() {
        return new JsonBackwardsCompatibilityTestConfiguration()
                .withBasePackage(DEFAULT_BASE_PACKAGE)
                .withRootFolder(DEFAULT_ROOT_FOLDER)
                .withEnhancedRandom(enhancedRandomBuilder().build());
    }

    protected EnhancedRandomBuilder enhancedRandomBuilder() {
        return RandomizationTestConstants.baseEnhancedRandomBuilder();
    }

    <T> void assertSubclassesAreBackwardsCompatible(String folderName, Class<T> baseClass) throws Throwable {
        Collection<Class<?>> subclasses = findAllNonAbstractSubclassesOf(baseClass);
        jsonBackwardsCompatibilityAsserter().assertJsonIsBackwardsCompatibleFor(folder(folderName), subclasses);
    }

    <T extends Annotation> void assertAnnotatedClassesAreBackwardsCompatible(String folderName, Class<T> baseAnnotation) throws Throwable {
        Collection<Class<?>> subclasses = findAllNonAbstractClassesAnnotatedWith(baseAnnotation);
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

    private File folder(String folderName) {
        return new File(cachedTestConfiguration().getRootFolder(), folderName);
    }

    private <T> Collection<Class<?>> findAllNonAbstractSubclassesOf(Class<T> baseClass) {
        return reflections.getSubTypesOf(baseClass)
                .stream()
                .filter(nonAbstractClasses())
                .collect(Collectors.toSet());
    }

    private <T extends Annotation> Collection<Class<?>> findAllNonAbstractClassesAnnotatedWith(Class<T> baseClass) {
        return reflections.getTypesAnnotatedWith(baseClass)
                .stream()
                .filter(nonAbstractClasses())
                .collect(Collectors.toSet());
    }

    private static <T> Predicate<Class<? extends T>> nonAbstractClasses() {
        return clazz -> !Modifier.isAbstract(clazz.getModifiers());
    }
}
