package be.butskri.playground.randombeans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import static be.butskri.playground.randombeans.DeepAssertions.assertDeepNoNullValues;
import static java.nio.charset.Charset.forName;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class JsonBackwardsCompatibilityTest {

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    private EnhancedRandom enhancedRandom;
    private ObjectMapper objectMapper;
    private File resultBaseFolder = new File("src/test/resources/backwardscompatibility/json");

    @Before
    public void setUp() {
        enhancedRandom = enhance(baseEnhancedRandomBuilder()).build();
        objectMapper = new ObjectMapper();
        objectMapper.addMixIn(SuperBean.class, SuperBeanMixin.class);
        objectMapper.registerModules(new DateTimeSerializationModule(), new CustomTypesModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Test
    public void jsonIsBackwardsCompatible() {
        assertJsonIsBackwardsCompatibleFor("sample", SomeBean.class, SubBeanOne.class, SubBeanTwo.class);
    }

    protected void assertJsonIsBackwardsCompatibleFor(String type, Class<?>... classes) {
        assertJsonIsBackwardsCompatibleFor(type, Arrays.asList(classes));
    }

    protected void assertJsonIsBackwardsCompatibleFor(String type, List<Class<?>> classes) {
        classes.stream()
                .map(clazz -> new JsonBackwardsCompatibilityAsserter(type, clazz))
                .forEach(asserter -> errorCollector.checkSucceeds(asserter::assertBackwardsCompatibility));
    }

    protected ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    protected EnhancedRandomBuilder enhance(EnhancedRandomBuilder baseEnhancedRandomBuilder) {
        return baseEnhancedRandomBuilder
                .randomize(SimplifiedInss.class, (Supplier<SimplifiedInss>) this::generateRandomInss);
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

    private SimplifiedInss generateRandomInss() {
        String inss = RandomStringUtils.randomNumeric(11);
        return new SimplifiedInss(inss);
    }

    private File actualFolder(String type) {
        return folder(type, "actual");
    }

    private File expectedFolder(String type) {
        return folder(type, "expected");
    }

    private File folder(String folder1, String folder2) {
        return new File(new File(resultBaseFolder, folder1), folder2);
    }

    private class JsonBackwardsCompatibilityAsserter {

        private String type;
        private Class<?> clazz;

        JsonBackwardsCompatibilityAsserter(String type, Class<?> clazz) {
            this.type = type;
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
            return file(actualFolder(type));
        }

        private File expectedFile() {
            return file(expectedFolder(type));
        }

        private File file(File folder) {
            return new File(folder, clazz.getSimpleName() + ".json");
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
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, object);
            } catch (IOException e) {
                fail(String.format("Problem writing object of type %s. Could not write json to file %s", clazz, file), e);
            }
        }
    }
}
