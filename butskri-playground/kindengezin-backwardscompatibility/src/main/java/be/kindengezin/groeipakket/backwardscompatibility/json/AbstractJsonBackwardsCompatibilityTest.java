package be.kindengezin.groeipakket.backwardscompatibility.json;

import be.kindengezin.groeipakket.backwardscompatibility.json.assertions.JsonBackwardsCompatibilityAsserter;
import be.kindengezin.groeipakket.backwardscompatibility.json.assertions.MetadataBackwardsCompatibilityAsserter;
import be.kindengezin.groeipakket.backwardscompatibility.json.random.RandomizationTestConstants;
import be.kindengezin.groeipakket.commons.domain.event.Event;
import be.kindengezin.groeipakket.domain.read.ViewObject;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static be.kindengezin.groeipakket.backwardscompatibility.json.util.MyFileUtils.cleanDirectory;

public abstract class AbstractJsonBackwardsCompatibilityTest {

    private File DEFAULT_ROOT_FOLDER = new File("src/test/resources/backwardscompatibility");

    private Reflections reflections;

    @Before
    public void setUpReflections() {
        this.reflections = new Reflections(getBasePackage());
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
        File baseFolder = new File(getRootFolder(), "metadata/events");
        cleanDirectory(new File(baseFolder, "actual"));
        metadataBackwardsCompatibilityAsserter().assertAnnotationsForEvents(baseFolder, findAllNonAbstractSubclassesOf(Event.class));
    }

    @Test
    public void deepPersonalDataMetadataIsBackwardsCompatible() throws Throwable {
        File baseFolder = new File(getRootFolder(), "metadata/deeppersonaldata");
        cleanDirectory(new File(baseFolder, "actual"));
        metadataBackwardsCompatibilityAsserter()
                .assertGdprAnnotations(baseFolder, backwardsCompatibilityAsserterConfiguration().getDeepPersonalDataClasses());
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

    <T extends Annotation> void assertAnnotatedClassesAreBackwardsCompatible(String folderName, Class<T> baseAnnotation) throws Throwable {
        Collection<Class<?>> subclasses = findAllNonAbstractClassesAnnotatedWith(baseAnnotation);
        jsonBackwardsCompatibilityAsserter().assertJsonIsBackwardsCompatibleFor(new File(getRootFolder(), folderName), subclasses);
    }

    protected JsonBackwardsCompatibilityTestConfiguration backwardsCompatibilityAsserterConfiguration() {
        return new JsonBackwardsCompatibilityTestConfiguration()
                .withObjectMapper(getObjectMapper())
                .withEnhancedRandom(enhancedRandomBuilder().build());
    }

    protected EnhancedRandomBuilder enhancedRandomBuilder() {
        return RandomizationTestConstants.baseEnhancedRandomBuilder();
    }

    private JsonBackwardsCompatibilityAsserter jsonBackwardsCompatibilityAsserter() {
        return new JsonBackwardsCompatibilityAsserter(backwardsCompatibilityAsserterConfiguration());
    }

    private MetadataBackwardsCompatibilityAsserter metadataBackwardsCompatibilityAsserter() {
        return new MetadataBackwardsCompatibilityAsserter(backwardsCompatibilityAsserterConfiguration());
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

    private <T> Predicate<Class<? extends T>> nonAbstractClasses() {
        return clazz -> !Modifier.isAbstract(clazz.getModifiers());
    }
}
