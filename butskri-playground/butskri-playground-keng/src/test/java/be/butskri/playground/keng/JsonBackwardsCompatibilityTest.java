package be.butskri.playground.keng;

import be.butskri.playground.keng.commons.test.AbstractBackwardsCompatibilityTest;
import be.butskri.playground.keng.myservice.beans.SimplifiedInss;
import be.butskri.playground.keng.myservice.beans.SuperBean;
import be.butskri.playground.keng.myservice.configuration.CustomTypesModule;
import be.butskri.playground.keng.myservice.configuration.DateTimeSerializationModule;
import be.butskri.playground.keng.myservice.configuration.SuperBeanMixin;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;

import java.util.function.Supplier;

public class JsonBackwardsCompatibilityTest extends AbstractBackwardsCompatibilityTest {

    private ObjectMapper objectMapper;

    @Before
    public void configureObjectMapper() {
        objectMapper = new ObjectMapper();
        objectMapper.addMixIn(SuperBean.class, SuperBeanMixin.class);
        objectMapper.registerModules(new DateTimeSerializationModule(), new CustomTypesModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Override
    protected String getBasePackage() {
        return "be.butskri";
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
