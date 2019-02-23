package be.butskri.playground.keng.commons.test.metadata;

import be.butskri.playground.keng.axon.DataSubjectId;
import be.butskri.playground.keng.axon.DeepPersonalData;
import be.butskri.playground.keng.axon.PersonalData;
import be.butskri.playground.keng.commons.annotations.CorrelationId;
import be.butskri.playground.keng.commons.events.Event;
import be.butskri.playground.keng.commons.events.IntegrationEvent;
import be.butskri.playground.keng.commons.test.AbstractJsonTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class AbstractEventMetadataBackwardsCompatibilityTest extends AbstractJsonTest {

    private File resultBaseFolder = new File("src/test/resources/backwardscompatibility/metadata");

    @Test
    public void eventAnnotationsShouldBeBackwardsCompatible() {
        clearDirectory(new File(resultBaseFolder, "actual"));
        assertAnnotationsForEvents(resultBaseFolder, findEventsToBeChecked());
    }

    protected Collection<Class<?>> findEventsToBeChecked() {
        return findAllNonAbstractSubclassesOf(Event.class);
    }

    protected void assertAnnotationsForEvents(File parentFolder, Collection<Class<?>> classes) {
        Collection<ClassMetaDataAsserter> asserters = annotationAssertersFor(parentFolder, classes);
        assertGdprAnnotationsUsedCorrectly(asserters);
        assertSubjectIdPresentWhenPersonalDataAnnotationsPresent(asserters);
        assertCorrelationIdPresentForIntegrationEvent(asserters);

        Collection<Class<?>> nestedDeepPersonalDataClasses = allNestedDeepPersonalDataClasses(asserters);
        assertGdprAnnotationsUsedCorrectly(annotationAssertersFor(parentFolder, nestedDeepPersonalDataClasses));
    }

    private Collection<ClassMetaDataAsserter> annotationAssertersFor(File parentFolder, Collection<Class<?>> classes) {
        return classes.stream()
                .map(clazz -> new ClassMetaDataAsserter(clazz, getObjectMapper(), parentFolder))
                .collect(Collectors.toList());
    }

    private void assertGdprAnnotationsUsedCorrectly(Collection<ClassMetaDataAsserter> asserters) {
        asserters.forEach(asserter -> asserter.assertGdprAnnotationsUsedCorrectly(errorCollector));
    }

    private Collection<Class<?>> allNestedDeepPersonalDataClasses(Collection<ClassMetaDataAsserter> asserters) {
        return asserters.stream()
                .map(ClassMetaDataAsserter::classInfo)
                .map(ClassInfo::getNestedDeepPersonalDataClasses)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private void assertSubjectIdPresentWhenPersonalDataAnnotationsPresent(Collection<ClassMetaDataAsserter> asserters) {
        asserters.forEach(asserter -> errorCollector.checkSucceeds(asserter::assertSubjectIdPresentWhenPersonalDataAnnotationsPresent));
    }

    private void assertCorrelationIdPresentForIntegrationEvent(Collection<ClassMetaDataAsserter> asserters) {
        asserters.forEach(asserter -> errorCollector.checkSucceeds(asserter::assertExactlyOneCorrelationIdPresentForIntegrationEvent));
    }

    private static <T> Predicate<T> not(Predicate<T> predicate) {
        return predicate.negate();
    }

    private void clearDirectory(File directory) {
        try {
            FileUtils.cleanDirectory(directory);
        } catch (IOException e) {
            Assertions.fail(String.format("Could not clean directory %s", directory), e);
        }
    }

    static class ClassMetaDataAsserter {

        private ObjectMapper objectMapper;
        private File parentFolder;
        private Class<?> clazz;
        private ClassInfo classInfo;

        public ClassMetaDataAsserter(Class<?> clazz, ObjectMapper objectMapper, File parentFolder) {
            this.clazz = clazz;
            this.objectMapper = objectMapper;
            this.parentFolder = parentFolder;
        }

        public void assertGdprAnnotationsUsedCorrectly(ErrorCollector errorCollector) {
            errorCollector.checkSucceeds(this::assertAtMost1DataSubjectId);
            errorCollector.checkSucceeds(this::assertPersonalDataFields);
            errorCollector.checkSucceeds(this::assertDeepPersonalDataFields);

            errorCollector.checkSucceeds(this::assertClassMetadataRemainsSame);
        }

        private ClassMetaDataAsserter assertClassMetadataRemainsSame() {
            String readJson = readJson();
            ClassMetadata classMetadata = classMetadata();
            if (readJson == null) {
                writeClassMetadata(classMetadata, expectedFile());
            } else {
                try {
                    Assertions.assertThat(jsonFor(classMetadata))
                            .describedAs("metadata for class %s should remain the same", clazz)
                            .isEqualTo(readJson);
                } catch (AssertionError error) {
                    writeClassMetadata(classMetadata, actualFile());
                    throw error;
                }
            }

            return this;
        }

        private String jsonFor(Object object) {
            try {
                return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
            } catch (JsonProcessingException e) {
                Assertions.fail(String.format("Problem converting object to json %s", object), e);
                return null;
            }
        }

        private void writeClassMetadata(ClassMetadata classMetadata, File file) {
            try {
                file.getParentFile().mkdirs();
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, classMetadata);
            } catch (IOException e) {
                Assertions.fail(String.format("Problem writing class metadata. Could not write json to file %s", file), e);
            }
        }

        private File actualFile() {
            return file("actual");
        }

        private File expectedFile() {
            return file("expected");
        }

        private File file(String subfolder) {
            File folder = new File(this.parentFolder, subfolder);
            return new File(folder, clazz.getSimpleName() + ".metadata");
        }

        private String readJson() {
            File file = expectedFile();
            if (file.exists()) {
                try {
                    return FileUtils.readFileToString(file, "UTF-8");
                } catch (IOException e) {
                    Assertions.fail(String.format("Problem reading file %s", file), e);
                }
            }
            return null;
        }

        private ClassMetadata classMetadata() {
            return ClassMetadata.builder()
                    .withCorrelationIdInfo(classInfo().findSingleFieldInfoByAnnotation(CorrelationId.class).orElse(null))
                    .withDataSubjectIdInfo(classInfo().findSingleFieldInfoByAnnotation(DataSubjectId.class).orElse(null))
                    .withPersonalDataFieldsInfos(classInfo().findFieldInfoCollectionByAnnotation(PersonalData.class))
                    .withDeepPersonalDataFieldsInfos(classInfo().findFieldInfoCollectionByAnnotation(DeepPersonalData.class))
                    .build();
        }

        public ClassMetaDataAsserter assertSubjectIdPresentWhenPersonalDataAnnotationsPresent() {
            if (hasPersonalDataFields()) {
                Assertions.assertThat(classInfo().findFieldInfoCollectionByAnnotation(DataSubjectId.class))
                        .describedAs("clazz %s containing personal data should have @DataSubjectId", clazz)
                        .hasSize(1);
            }
            return this;
        }

        public ClassMetaDataAsserter assertExactlyOneCorrelationIdPresentForIntegrationEvent() {
            if (clazz.isAssignableFrom(IntegrationEvent.class))
                Assertions.assertThat(classInfo().findFieldInfoCollectionByAnnotation(CorrelationId.class))
                        .describedAs("%s should have exactly one @CorrelationId", clazz)
                        .hasSize(1);
            return this;
        }

        private boolean hasPersonalDataFields() {
            return !classInfo().findFieldInfoCollectionByAnnotation(PersonalData.class).isEmpty()
                    || !classInfo().findFieldInfoCollectionByAnnotation(DeepPersonalData.class).isEmpty();
        }

        private ClassMetaDataAsserter assertAtMost1DataSubjectId() {
            Assertions.assertThat(classInfo().findFieldInfoCollectionByAnnotation(DataSubjectId.class).size())
                    .describedAs("class %s should have maximum 1 field annotated with @DatasubjectId", clazz)
                    .isLessThanOrEqualTo(1);
            return this;
        }

        private ClassMetaDataAsserter assertPersonalDataFields() {
            Assertions.assertThat(classInfo().findFieldInfoCollectionByAnnotation(PersonalData.class).stream().filter(not(FieldInfo::canBeAnnotatedWithPersonalData)))
                    .describedAs("Fields in class %s annotated with @PersonalData that shouldn't", clazz)
                    .isEmpty();
            return this;
        }

        private ClassMetaDataAsserter assertDeepPersonalDataFields() {
            Assertions.assertThat(classInfo().findFieldInfoCollectionByAnnotation(DeepPersonalData.class).stream().filter(not(FieldInfo::canBeAnnotatedWithDeepPersonalData)))
                    .describedAs("Fields in class %s annotated with @DeepPersonalData that shouldn't", clazz)
                    .isEmpty();
            return this;
        }

        private ClassInfo classInfo() {
            if (this.classInfo == null) {
                this.classInfo = ClassInfo.classInfoFor(clazz);
            }
            return classInfo;
        }
    }

}
