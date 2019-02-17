package be.butskri.playground.keng.commons.test.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ErrorCollector;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static be.butskri.playground.keng.commons.test.json.DeepAssertions.assertDeepNoNullValues;
import static java.nio.charset.Charset.forName;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public abstract class GenericAbstractJsonBackwardsCompatibilityTest {

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    private EnhancedRandom enhancedRandom;

    @Before
    public void setUpRandomizer() {
        enhancedRandom = enhance(baseEnhancedRandomBuilder()).build();
    }

    protected abstract ObjectMapper getObjectMapper();

    protected abstract EnhancedRandomBuilder enhance(EnhancedRandomBuilder baseEnhancedRandomBuilder);

    protected void assertJsonIsBackwardsCompatibleFor(File baseFolder, Class<?>... classes) {
        assertJsonIsBackwardsCompatibleFor(baseFolder, Arrays.asList(classes));
    }

    protected void assertJsonIsBackwardsCompatibleFor(File baseFolder, List<Class<?>> classes) {
        classes.stream()
                .map(clazz -> new JsonBackwardsCompatibilityAsserter(baseFolder, clazz))
                .forEach(asserter -> errorCollector.checkSucceeds(asserter::assertBackwardsCompatibility));
        errorCollector.checkSucceeds(() -> assertAllExpectedJsonFilesAreCoveredBy(baseFolder, classes));
    }

    private GenericAbstractJsonBackwardsCompatibilityTest assertAllExpectedJsonFilesAreCoveredBy(File baseFolder, List<Class<?>> classes) {
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

    private List<String> jsonFilesInFolder(File expectedFilesFolder) {
        return Stream.of(expectedFilesFolder.listFiles((file) -> file.getName().endsWith(".json")))
                .map(file -> file.getName())
                .sorted()
                .collect(Collectors.toList());
    }

    private List<String> filenamesForClasses(List<Class<?>> classes) {
        return classes.stream()
                .map(Class::getSimpleName)
                .map(classname -> classname + ".json")
                .sorted()
                .collect(Collectors.toList());
    }

    private EnhancedRandomBuilder baseEnhancedRandomBuilder() {
        return EnhancedRandomBuilder.aNewEnhancedRandomBuilder()
                .randomizationDepth(3)
                .charset(forName("UTF-8"))
                .stringLengthRange(5, 50)
                .collectionSizeRange(2, 2)
                .scanClasspathForConcreteTypes(true)
                .overrideDefaultInitialization(true);
    }

    private class JsonBackwardsCompatibilityAsserter {

        private File baseFolder;
        private Class<?> clazz;

        JsonBackwardsCompatibilityAsserter(File baseFolder, Class<?> clazz) {
            this.baseFolder = baseFolder;
            this.clazz = clazz;
        }

        JsonBackwardsCompatibilityAsserter assertBackwardsCompatibility() {
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
            assertDeepNoNullValues(object);
        }

        private void cleanupActualFile() {
            actualFile().delete();
        }

        private Object loadExpectedObject() {
            return loadObject(expectedFile());
        }

        private Object loadActualObject() {
            return loadObject(actualFile());
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
            return new File(folder, clazz.getSimpleName() + ".json");
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
                return getObjectMapper().readValue(file, clazz);
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
                getObjectMapper().writerWithDefaultPrettyPrinter().writeValue(file, object);
            } catch (IOException e) {
                fail(String.format("Problem writing object of type %s. Could not write json to file %s", clazz, file), e);
            }
        }
    }
}
