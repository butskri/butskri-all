package be.butskri.playground.randombeans;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import static java.nio.charset.Charset.forName;
import static org.assertj.core.api.Assertions.assertThat;

public class JsonBackwardsCompatibilityTest {

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
                .forEach(JsonBackwardsCompatibilityAsserter::assertBackwardsCompatibility);
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

        void assertBackwardsCompatibility() {
            generateJsonWhenNecessary();
            deserializeAndThenSerializeGivesSameObject();
        }

        private void generateJsonWhenNecessary() {
            if (!expectedFile().exists()) {
                Object randmobBean = enhancedRandom.nextObject(clazz);
                writeValueToFile(expectedFile(), randmobBean);
            }
        }

        private void deserializeAndThenSerializeGivesSameObject() {
            Object object = null;
            try {
                object = objectMapper.readValue(expectedFile(), clazz);
                String foundValue = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
                String expectedValue = org.apache.commons.io.FileUtils.readFileToString(expectedFile(), "UTF-8");
                assertThat(foundValue)
                        .describedAs("json for clazz %s not deserialized/serialized correctly", clazz)
                        .isEqualTo(expectedValue);
            } catch (Error | RuntimeException throwable) {
                writeValueToFile(actualFile(), object);
                throw throwable;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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

        private void writeValueToFile(File file, Object object) {
            try {
                file.getParentFile().mkdirs();
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, object);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
