package be.butskri.playground.keng.commons.backwardscompatibility.json.assertions;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.apache.commons.io.FileUtils;
import org.junit.rules.ErrorCollector;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static be.butskri.playground.keng.commons.backwardscompatibility.json.assertions.DeepAssertions.assertNoDeepNullValues;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class JsonBackwardsCompatibilityAsserter extends ErrorCollector {

    private ObjectMapper objectMapper;
    private EnhancedRandom enhancedRandom;

    public JsonBackwardsCompatibilityAsserter(ObjectMapper objectMapper, EnhancedRandom enhancedRandom) {
        this.objectMapper = objectMapper;
        this.enhancedRandom = enhancedRandom;
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

    private static String fileNameFor(Class<?> clazz) {
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
            cleanupActualFile();
            assertObjectDoesNotContainNullValues();
            return this;
        }

        private void generateJsonWhenNecessary() {
            if (!expectedFile().exists()) {
                Object randmobBean = enhancedRandom.nextObject(clazz);
                writeObjectToFile(randmobBean, expectedFile());
            }
        }

        private void deserializeAndThenSerializeObject() {
            Object expectedObject = loadExpectedObject();
            writeObjectToFile(expectedObject, actualFile());
        }

        private void assertActualAndExpectedJsonsAreTheSame() {
            String expectedJson = loadExpectedJson();
            String actualJson = loadActualJson();

            assertThat(actualJson)
                    .describedAs("json for clazz %s not deserialized/serialized correctly", clazz)
                    .isEqualTo(expectedJson);
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
                return objectMapper.readValue(file, clazz);
            } catch (IOException e) {
                fail(String.format("Problem loading object of type %s. Could not read json from file %s", clazz, file), e);
                return null;
            }
        }

        private String loadJson(File file) {
            if (!file.exists()) {
                return null;
            }
            try {
                return FileUtils.readFileToString(file, "UTF-8");
            } catch (IOException e) {
                fail(String.format("Problem reading file %s", file), e);
                return null;
            }
        }

        private void writeObjectToFile(Object object, File file) {
            try {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                if (!file.exists()) {
                    file.createNewFile();
                }
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, object);
            } catch (IOException e) {
                fail(String.format("Problem writing object of type %s. Could not write json to file %s", clazz, file), e);
            }
        }
    }
}
