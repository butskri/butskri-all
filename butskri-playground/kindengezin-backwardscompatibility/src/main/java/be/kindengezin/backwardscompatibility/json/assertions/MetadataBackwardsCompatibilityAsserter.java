package be.kindengezin.backwardscompatibility.json.assertions;

import io.axoniq.gdpr.api.DataSubjectId;
import io.axoniq.gdpr.api.DeepPersonalData;
import io.axoniq.gdpr.api.PersonalData;
import be.kindengezin.groeipakket.commons.integration.annotations.producer.CorrelationId;
import be.kindengezin.backwardscompatibility.json.metadata.ClassInfo;
import be.kindengezin.backwardscompatibility.json.metadata.ClassMetadata;
import be.kindengezin.backwardscompatibility.json.metadata.FieldInfo;
import be.kindengezin.backwardscompatibility.json.util.JsonUtils;
import be.kindengezin.groeipakket.commons.domain.event.IntegrationEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.rules.ErrorCollector;

import java.io.File;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static be.kindengezin.backwardscompatibility.json.util.JsonUtils.loadJson;
import static be.kindengezin.backwardscompatibility.json.util.JsonUtils.writeJsonToFile;
import static be.kindengezin.backwardscompatibility.json.assertions.JsonAssertions.assertJsonEqual;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class MetadataBackwardsCompatibilityAsserter extends ErrorCollector {

    private JsonBackwardsCompatibilityAsserterConfiguration configuration;

    public MetadataBackwardsCompatibilityAsserter(JsonBackwardsCompatibilityAsserterConfiguration configuration) {
        this.configuration = configuration;
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
                .map(clazz -> new ClassMetaDataAsserter(clazz, parentFolder))
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

    private ObjectMapper objectMapper() {
        return configuration.getObjectMapper();
    }

    public static String fileNameFor(Class<?> clazz) {
        return clazz.getSimpleName() + ".metadata";
    }

    class ClassMetaDataAsserter {

        private File parentFolder;
        private Class<?> clazz;
        private ClassInfo classInfo;

        public ClassMetaDataAsserter(Class<?> clazz, File parentFolder) {
            this.clazz = clazz;
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
            generateJsonWhenNecessary();
            ClassMetadata classMetadata = classMetadata();
            try {
                assertJsonEqual(
                        String.format("metadata for %s should remain the same", clazz),
                        loadJson(expectedFile()),
                        jsonFor(classMetadata)
                );
            } catch (AssertionError error) {
                writeClassMetadata(classMetadata, actualFile());
                throw error;
            }
            return this;
        }

        private void generateJsonWhenNecessary() {
            if (!expectedFile().exists() && configuration.isFailOnMissingExpectedFile()) {
                fail(String.format("Metadata file %s missing for %s. " +
                                "Probably you created a new event or added a new @DeepPersonalData field. " +
                                "You can generate the expected file using JsonBackwardsCompatibilityAsserterConfiguration.withFailOnMissingExpectedFile(false)",
                        expectedFile(),
                        clazz));
            }
            if (!expectedFile().exists()) {
                writeClassMetadata(classMetadata(), expectedFile());
            }
        }

        private String jsonFor(Object object) {
            return JsonUtils.jsonFor(object, objectMapper());
        }

        private void writeClassMetadata(ClassMetadata classMetadata, File file) {
            writeJsonToFile(classMetadata, file, objectMapper());
        }

        private File actualFile() {
            return file("actual");
        }

        private File expectedFile() {
            return file("expected");
        }

        private File file(String subfolder) {
            File folder = new File(this.parentFolder, subfolder);
            return new File(folder, fileNameFor(clazz));
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
