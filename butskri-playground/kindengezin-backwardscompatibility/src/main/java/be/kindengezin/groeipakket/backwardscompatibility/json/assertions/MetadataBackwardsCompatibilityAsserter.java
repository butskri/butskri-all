package be.kindengezin.groeipakket.backwardscompatibility.json.assertions;

import be.kindengezin.groeipakket.backwardscompatibility.json.reflection.ClassInfo;
import be.kindengezin.groeipakket.backwardscompatibility.json.reflection.ClassMetadata;
import be.kindengezin.groeipakket.backwardscompatibility.json.reflection.FieldInfo;
import be.kindengezin.groeipakket.backwardscompatibility.json.util.JsonUtils;
import be.kindengezin.groeipakket.commons.domain.event.IntegrationEvent;
import be.kindengezin.groeipakket.commons.integration.annotations.producer.CorrelationId;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.axoniq.gdpr.api.DataSubjectId;
import io.axoniq.gdpr.api.DeepPersonalData;
import io.axoniq.gdpr.api.PersonalData;
import org.junit.rules.ErrorCollector;

import java.io.File;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static be.kindengezin.groeipakket.backwardscompatibility.json.assertions.JsonAssertions.assertJsonEqual;
import static be.kindengezin.groeipakket.backwardscompatibility.json.util.MyFileUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class MetadataBackwardsCompatibilityAsserter extends ErrorCollector {

    private MetadataBackwardsCompatibilityAsserterConfiguration configuration;

    public MetadataBackwardsCompatibilityAsserter(MetadataBackwardsCompatibilityAsserterConfiguration configuration) {
        this.configuration = configuration;
    }

    public void assertAnnotationsForEvents(File baseFolder, Collection<Class<?>> classes) throws Throwable {
        cleanDirectory(new File(baseFolder, "actual"));
        Collection<ClassMetaDataAsserter> asserters = annotationAssertersFor(baseFolder, classes);
        assertGdprAnnotationsUsedCorrectly(asserters);
        assertSubjectIdPresentWhenPersonalDataAnnotationsPresent(asserters);
        assertCorrelationIdPresentForIntegrationEvent(asserters);
        verify();
    }

    public void assertGdprAnnotations(File baseFolder, Collection<Class<?>> classes) throws Throwable {
        cleanDirectory(new File(baseFolder, "actual"));
        Collection<ClassMetaDataAsserter> asserters = annotationAssertersFor(baseFolder, classes);
        assertGdprAnnotationsUsedCorrectly(asserters);
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
            if (clazz.isAssignableFrom(IntegrationEvent.class)) {
                assertThat(classInfo().findFieldInfoCollectionByAnnotation(CorrelationId.class))
                        .describedAs("%s should have exactly one @CorrelationId", clazz)
                        .hasSize(1);
            }
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
            if (!expectedFile().exists() && configuration.isFailOnMissingExpectedFileEnabled()) {
                fail(String.format("Metadata file %s missing for %s. " +
                                "Probably you created a new event or added a new @DeepPersonalData field. " +
                                "You can generate the expected file using JsonBackwardsCompatibilityTestConfiguration.withFailOnMissingExpectedFileEnabled(false)",
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
