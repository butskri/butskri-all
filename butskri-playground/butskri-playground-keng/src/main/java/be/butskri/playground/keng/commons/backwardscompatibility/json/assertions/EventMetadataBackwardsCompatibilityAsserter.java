package be.butskri.playground.keng.commons.backwardscompatibility.json.assertions;

import be.butskri.playground.keng.axon.DataSubjectId;
import be.butskri.playground.keng.axon.DeepPersonalData;
import be.butskri.playground.keng.axon.PersonalData;
import be.butskri.playground.keng.commons.annotations.CorrelationId;
import be.butskri.playground.keng.commons.backwardscompatibility.json.metadata.ClassInfo;
import be.butskri.playground.keng.commons.backwardscompatibility.json.metadata.ClassMetadata;
import be.butskri.playground.keng.commons.backwardscompatibility.json.metadata.FieldInfo;
import be.butskri.playground.keng.commons.backwardscompatibility.json.util.JsonUtils;
import be.butskri.playground.keng.commons.events.IntegrationEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.rules.ErrorCollector;

import java.io.File;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static be.butskri.playground.keng.commons.backwardscompatibility.json.util.JsonUtils.loadJson;
import static be.butskri.playground.keng.commons.backwardscompatibility.json.util.JsonUtils.writeJsonToFile;
import static org.assertj.core.api.Assertions.assertThat;

public class EventMetadataBackwardsCompatibilityAsserter extends ErrorCollector {

    private ObjectMapper objectMapper;

    public EventMetadataBackwardsCompatibilityAsserter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void assertAnnotationsForEvents(File parentFolder, Collection<Class<?>> classes) throws Throwable {
        Collection<ClassMetaDataAsserter> asserters = annotationAssertersFor(parentFolder, classes);
        assertGdprAnnotationsUsedCorrectly(asserters);
        assertSubjectIdPresentWhenPersonalDataAnnotationsPresent(asserters);
        assertCorrelationIdPresentForIntegrationEvent(asserters);

        Collection<Class<?>> nestedDeepPersonalDataClasses = allNestedDeepPersonalDataClasses(asserters);
        assertGdprAnnotationsUsedCorrectly(annotationAssertersFor(parentFolder, nestedDeepPersonalDataClasses));
        verify();
    }

    private Collection<ClassMetaDataAsserter> annotationAssertersFor(File parentFolder, Collection<Class<?>> classes) {
        return classes.stream()
                .map(clazz -> new ClassMetaDataAsserter(clazz, objectMapper, parentFolder))
                .collect(Collectors.toList());
    }

    private void assertGdprAnnotationsUsedCorrectly(Collection<ClassMetaDataAsserter> asserters) {
        asserters.forEach(asserter -> asserter.assertGdprAnnotationsUsedCorrectly(this));
    }

    private Collection<Class<?>> allNestedDeepPersonalDataClasses(Collection<ClassMetaDataAsserter> asserters) {
        return asserters.stream()
                .map(ClassMetaDataAsserter::classInfo)
                .map(ClassInfo::getNestedDeepPersonalDataClasses)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private void assertSubjectIdPresentWhenPersonalDataAnnotationsPresent(Collection<ClassMetaDataAsserter> asserters) {
        asserters.forEach(asserter -> checkSucceeds(asserter::assertSubjectIdPresentWhenPersonalDataAnnotationsPresent));
    }

    private void assertCorrelationIdPresentForIntegrationEvent(Collection<ClassMetaDataAsserter> asserters) {
        asserters.forEach(asserter -> checkSucceeds(asserter::assertExactlyOneCorrelationIdPresentForIntegrationEvent));
    }

    private static <T> Predicate<T> not(Predicate<T> predicate) {
        return predicate.negate();
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

        public ClassMetaDataAsserter assertSubjectIdPresentWhenPersonalDataAnnotationsPresent() {
            if (hasPersonalDataFields()) {
                assertThat(classInfo().findFieldInfoCollectionByAnnotation(DataSubjectId.class))
                        .describedAs("clazz %s containing personal data should have @DataSubjectId", clazz)
                        .hasSize(1);
            }
            return this;
        }

        public ClassMetaDataAsserter assertExactlyOneCorrelationIdPresentForIntegrationEvent() {
            if (clazz.isAssignableFrom(IntegrationEvent.class))
                assertThat(classInfo().findFieldInfoCollectionByAnnotation(CorrelationId.class))
                        .describedAs("%s should have exactly one @CorrelationId", clazz)
                        .hasSize(1);
            return this;
        }

        public void assertGdprAnnotationsUsedCorrectly(ErrorCollector errorCollector) {
            errorCollector.checkSucceeds(this::assertAtMost1DataSubjectId);
            errorCollector.checkSucceeds(this::assertPersonalDataFields);
            errorCollector.checkSucceeds(this::assertDeepPersonalDataFields);
            errorCollector.checkSucceeds(this::assertClassMetadataRemainsSame);
        }

        private ClassMetaDataAsserter assertAtMost1DataSubjectId() {
            assertThat(classInfo().findFieldInfoCollectionByAnnotation(DataSubjectId.class).size())
                    .describedAs("class %s should have maximum 1 field annotated with @DatasubjectId", clazz)
                    .isLessThanOrEqualTo(1);
            return this;
        }

        private ClassMetaDataAsserter assertPersonalDataFields() {
            assertThat(classInfo().findFieldInfoCollectionByAnnotation(PersonalData.class).stream().filter(not(FieldInfo::canBeAnnotatedWithPersonalData)))
                    .describedAs("Fields in class %s annotated with @PersonalData that shouldn't", clazz)
                    .isEmpty();
            return this;
        }

        private ClassMetaDataAsserter assertDeepPersonalDataFields() {
            assertThat(classInfo().findFieldInfoCollectionByAnnotation(DeepPersonalData.class).stream().filter(not(FieldInfo::canBeAnnotatedWithDeepPersonalData)))
                    .describedAs("Fields in class %s annotated with @DeepPersonalData that shouldn't", clazz)
                    .isEmpty();
            return this;
        }

        private ClassMetaDataAsserter assertClassMetadataRemainsSame() {
            String readJson = readJson();
            ClassMetadata classMetadata = classMetadata();
            if (readJson == null) {
                writeClassMetadata(classMetadata, expectedFile());
            } else {
                try {
                    assertThat(jsonFor(classMetadata))
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
            return JsonUtils.jsonFor(object, objectMapper);
        }

        private void writeClassMetadata(ClassMetadata classMetadata, File file) {
            writeJsonToFile(classMetadata, file, objectMapper);
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
            return loadJson(expectedFile());
        }

        private ClassMetadata classMetadata() {
            return ClassMetadata.builder()
                    .withCorrelationIdInfo(classInfo().findSingleFieldInfoByAnnotation(CorrelationId.class).orElse(null))
                    .withDataSubjectIdInfo(classInfo().findSingleFieldInfoByAnnotation(DataSubjectId.class).orElse(null))
                    .withPersonalDataFieldsInfos(classInfo().findFieldInfoCollectionByAnnotation(PersonalData.class))
                    .withDeepPersonalDataFieldsInfos(classInfo().findFieldInfoCollectionByAnnotation(DeepPersonalData.class))
                    .build();
        }

        private boolean hasPersonalDataFields() {
            return !classInfo().findFieldInfoCollectionByAnnotation(PersonalData.class).isEmpty()
                    || !classInfo().findFieldInfoCollectionByAnnotation(DeepPersonalData.class).isEmpty();
        }

        private ClassInfo classInfo() {
            if (this.classInfo == null) {
                this.classInfo = ClassInfo.classInfoFor(clazz);
            }
            return classInfo;
        }
    }
}
