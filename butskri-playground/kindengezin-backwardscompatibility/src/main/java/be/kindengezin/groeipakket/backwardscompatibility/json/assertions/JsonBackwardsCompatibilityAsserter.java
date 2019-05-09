package be.kindengezin.groeipakket.backwardscompatibility.json.assertions;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.rules.ErrorCollector;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static be.kindengezin.groeipakket.backwardscompatibility.json.assertions.DeepAssertions.assertNoDeepNullValues;
import static be.kindengezin.groeipakket.backwardscompatibility.json.util.MyFileUtils.loadJson;
import static be.kindengezin.groeipakket.backwardscompatibility.json.util.MyFileUtils.writeJsonToFile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class JsonBackwardsCompatibilityAsserter extends ErrorCollector {

    private JsonBackwardsCompatibilityAsserterConfiguration jsonBackwardsCompatibilityAsserterConfiguration;

    public JsonBackwardsCompatibilityAsserter(JsonBackwardsCompatibilityAsserterConfiguration jsonBackwardsCompatibilityAsserterConfiguration) {
        this.jsonBackwardsCompatibilityAsserterConfiguration = jsonBackwardsCompatibilityAsserterConfiguration;
    }

    public <T> void assertJsonIsBackwardsCompatibleFor(File baseFolder, Collection<Class<? extends T>> classes) throws Throwable {
        classes.stream()
                .map(clazz -> new SingleJsonBackwardsCompatibilityAsserter(baseFolder, clazz))
                .forEach(asserter -> checkSucceeds(asserter::assertBackwardsCompatibility));
        super.checkSucceeds(() -> assertAllExpectedJsonFilesAreCoveredBy(baseFolder, classes));
        verify();
    }

    private <T> JsonBackwardsCompatibilityAsserter assertAllExpectedJsonFilesAreCoveredBy(File baseFolder, Collection<Class<? extends T>> classes) {
        File expectedFilesFolder = new File(baseFolder, "expected");
        List<String> jsonFilesInFolder = jsonFilesInFolder(expectedFilesFolder);
        List<String> filenamesForClasses = filenamesForClasses(classes);
        assertThat(subtract(jsonFilesInFolder, filenamesForClasses))
                .describedAs("Files found in folder %s without matching class", expectedFilesFolder)
                .isEmpty();
        return this;
    }

    private List<String> subtract(List<String> collection, List<String> toBeRemoved) {
        List<String> result = new ArrayList<>(collection);
        result.removeAll(toBeRemoved);
        return result;
    }

    private List<String> jsonFilesInFolder(File folder) {
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return Stream.of(folder.listFiles((file) -> file.getName().endsWith(".json")))
                .map(file -> file.getName())
                .sorted()
                .collect(Collectors.toList());
    }

    private <T> List<String> filenamesForClasses(Collection<Class<? extends T>> classes) {
        return classes.stream()
                .map(JsonBackwardsCompatibilityAsserter::fileNameFor)
                .sorted()
                .collect(Collectors.toList());
    }

    private ObjectMapper objectMapper() {
        return jsonBackwardsCompatibilityAsserterConfiguration.getObjectMapper();
    }

    private EnhancedRandom enhancedRandom() {
        return jsonBackwardsCompatibilityAsserterConfiguration.getEnhancedRandom();
    }

    public static String fileNameFor(Class<?> clazz) {
        return clazz.getSimpleName() + ".json";
    }

    private class SingleJsonBackwardsCompatibilityAsserter {

        private File baseFolder;
        private Class<?> clazz;

        SingleJsonBackwardsCompatibilityAsserter(File baseFolder, Class<?> clazz) {
            this.baseFolder = baseFolder;
            this.clazz = clazz;
        }

        SingleJsonBackwardsCompatibilityAsserter assertBackwardsCompatibility() {
            generateJsonWhenNecessary();
            deserializeAndThenSerializeObject();
            assertActualAndExpectedJsonsAreTheSame();
            assertObjectDoesNotContainNullValues();
            cleanupActualFile();
            return this;
        }

        private void generateJsonWhenNecessary() {
            if (!expectedFile().exists() && jsonBackwardsCompatibilityAsserterConfiguration.isFailOnMissingExpectedFile()) {
                fail(String.format("No json found for %s in folder %s", clazz, expectedFolder()));
            }
            if (!expectedFile().exists()) {
                Object randomBean = enhancedRandom().nextObject(clazz);
                writeObjectToFile(randomBean, expectedFile());
            }
        }

        private void deserializeAndThenSerializeObject() {
            Object expectedObject = loadExpectedObject();
            writeObjectToFile(expectedObject, actualFile());
        }

        private void assertActualAndExpectedJsonsAreTheSame() {
            JsonAssertions.assertJsonEqual(
                    String.format("json for clazz %s not deserialized/serialized correctly", clazz),
                    loadExpectedJson(),
                    loadActualJson());
        }

        private void assertObjectDoesNotContainNullValues() {
            Object object = loadExpectedObject();
            assertNoDeepNullValues(object);
        }

        private void cleanupActualFile() {
            actualFile().delete();
        }

        private Object loadExpectedObject() {
            return loadObject(expectedFile());
        }

        private String loadExpectedJson() {
            return loadJson(expectedFile());
        }

        private String loadActualJson() {
            return loadJson(actualFile());
        }

        private File actualFile() {
            return file(actualFolder());
        }

        private File expectedFile() {
            return file(expectedFolder());
        }

        private File file(File folder) {
            return new File(folder, fileNameFor(clazz));
        }

        private File actualFolder() {
            return new File(baseFolder, "actual");
        }

        private File expectedFolder() {
            return new File(baseFolder, "expected");
        }

        private Object loadObject(File file) {
            if (!file.exists()) {
                return null;
            }
            try {
                return objectMapper().readValue(file, clazz);
            } catch (IOException e) {
                fail(String.format("Problem loading object of type %s. Could not read json from file %s", clazz, file), e);
                return null;
            }
        }

        private void writeObjectToFile(Object object, File file) {
            writeJsonToFile(object, file, objectMapper());
        }
    }
}
