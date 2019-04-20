package be.butskri.playground.keng.myservice;

import be.butskri.playground.keng.commons.backwardscompatibility.json.AbstractJsonBackwardsCompatibilityTest;
import be.butskri.playground.keng.commons.backwardscompatibility.json.assertions.ObjectMapperTestConstants;
import be.butskri.playground.keng.myservice.beans.SimplifiedInss;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.function.Supplier;

public class JsonBackwardsCompatibilityTest extends AbstractJsonBackwardsCompatibilityTest {

    @Override
    protected String getBasePackage() {
        return "be.butskri.playground.keng.myservice";
    }

    @Override
    protected ObjectMapper getObjectMapper() {
        return ObjectMapperTestConstants.objectMapperForTests();
    }

    @Override
    protected EnhancedRandomBuilder enhancedRandomBuilder() {
        return super.enhancedRandomBuilder()
                .randomize(SimplifiedInss.class, (Supplier<SimplifiedInss>) this::generateRandomInss);
    }

    private SimplifiedInss generateRandomInss() {
        String inss = RandomStringUtils.randomNumeric(11);
        return new SimplifiedInss(inss);
    }
}
