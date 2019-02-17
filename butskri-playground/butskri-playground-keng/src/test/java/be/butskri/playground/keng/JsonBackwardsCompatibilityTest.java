package be.butskri.playground.keng;

import be.butskri.test.backwardscompatibility.json.GenericAbstractJsonBackwardsCompatibilityTest;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.function.Supplier;

public class JsonBackwardsCompatibilityTest extends GenericAbstractJsonBackwardsCompatibilityTest {

    private ObjectMapper objectMapper;
    private File resultBaseFolder = new File("src/test/resources/backwardscompatibility/json");

    @Before
    public void configureObjectMapper() {
        objectMapper = new ObjectMapper();
        objectMapper.addMixIn(SuperBean.class, SuperBeanMixin.class);
        objectMapper.registerModules(new DateTimeSerializationModule(), new CustomTypesModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Test
    public void jsonIsBackwardsCompatible() {
        assertJsonIsBackwardsCompatibleFor(new File(resultBaseFolder, "sample"), SomeBean.class, SubBeanOne.class, SubBeanTwo.class);
    }

    @Override
    protected ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    @Override
    protected EnhancedRandomBuilder enhance(EnhancedRandomBuilder baseEnhancedRandomBuilder) {
        return baseEnhancedRandomBuilder
                .randomize(SimplifiedInss.class, (Supplier<SimplifiedInss>) this::generateRandomInss);
    }

    private SimplifiedInss generateRandomInss() {
        String inss = RandomStringUtils.randomNumeric(11);
        return new SimplifiedInss(inss);
    }
}
