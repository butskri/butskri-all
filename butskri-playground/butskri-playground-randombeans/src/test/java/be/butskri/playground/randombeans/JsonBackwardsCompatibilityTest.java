package be.butskri.playground.randombeans;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static java.nio.charset.Charset.forName;
import static org.assertj.core.api.Assertions.assertThat;

public class JsonBackwardsCompatibilityTest {

    private EnhancedRandom enhancedRandom;
    private ObjectMapper objectMapper;
    private File resultBaseFolder = new File("src/test/resources/backwardscompatibility/json");

    @Before
    public void setUp() {
        enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder()
                .randomizationDepth(3)
                .charset(forName("UTF-8"))
                .stringLengthRange(5, 50)
                .collectionSizeRange(2, 2)
                .scanClasspathForConcreteTypes(true)
                .overrideDefaultInitialization(true)
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.addMixIn(SuperBean.class, SuperBeanMixin.class);
        objectMapper.registerModules(dateTimeSerializationModule());
    }

    private Module dateTimeSerializationModule() {
        return new DateTimeSerializationModule();
    }

    @Test
    public void jsonIsBackwardsCompatible() {
        assertJsonIsBackwardsCompatibleFor(SomeBean.class, SubBeanOne.class, SubBeanTwo.class);
    }

    private void assertJsonIsBackwardsCompatibleFor(Class<?>... classes) {
        assertJsonIsBackwardsCompatibleFor(Arrays.asList(classes));
    }

    private void assertJsonIsBackwardsCompatibleFor(List<Class<?>> classes) {
        classes.stream()
                .map(clazz -> new JsonBackwardsCompatibilityAsserter(clazz))
                .forEach(JsonBackwardsCompatibilityAsserter::assertBackwardsCompatibility);
    }

    ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    private class JsonBackwardsCompatibilityAsserter {

        private Class<?> clazz;

        JsonBackwardsCompatibilityAsserter(Class<?> clazz) {
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
            try {
                Object object = objectMapper.readValue(expectedFile(), clazz);
                String foundValue = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
                String expectedValue = org.apache.commons.io.FileUtils.readFileToString(expectedFile(), "UTF-8");
                assertThat(foundValue)
                        .describedAs("json for clazz %s not deserialized/serialized correctly", clazz)
                        .isEqualTo(expectedValue);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private File expectedFile() {
            return new File(resultBaseFolder, "expected/" + clazz.getSimpleName() + ".json");
        }

        private void writeValueToFile(File file, Object randmobBean) {
            try {
                file.getParentFile().mkdirs();
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, randmobBean);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
