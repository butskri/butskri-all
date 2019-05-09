package be.kindengezin.myservice;

import be.kindengezin.groeipakket.backwardscompatibility.json.AbstractJsonBackwardsCompatibilityTest;
import be.kindengezin.groeipakket.backwardscompatibility.json.JsonBackwardsCompatibilityTestConfiguration;
import be.kindengezin.groeipakket.backwardscompatibility.json.assertions.ObjectMapperTestConstants;
import be.kindengezin.myservice.beans.SimplifiedInss;
import be.kindengezin.myservice.events.SomeDeepPersonalData;
import be.kindengezin.myservice.events.SomeDeeperPersonalData;
import be.kindengezin.myservice.events.SomeDeepestPersonalData;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Supplier;

public class JsonBackwardsCompatibilityTest extends AbstractJsonBackwardsCompatibilityTest {

    private static final Collection<Class<?>> DEEP_PERSONAL_DATA_CLASSES = Arrays.asList(
            SomeDeepPersonalData.class,
            SomeDeeperPersonalData.class,
            SomeDeepestPersonalData.class
    );

    @Override
    protected String getBasePackage() {
        return "be.kindengezin.myservice";
    }

    @Override
    protected ObjectMapper getObjectMapper() {
        return ObjectMapperTestConstants.objectMapperForTests();
    }

    @Override
    protected JsonBackwardsCompatibilityTestConfiguration backwardsCompatibilityAsserterConfiguration() {
        return super.backwardsCompatibilityAsserterConfiguration()
                .withDeepPersonalDataClasses(DEEP_PERSONAL_DATA_CLASSES);
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
