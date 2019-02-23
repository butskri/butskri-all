package be.butskri.playground.keng;

import be.butskri.playground.keng.commons.test.metadata.AbstractEventMetadataBackwardsCompatibilityTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;

public class EventMetadataBackwardsCompatibilityTest extends AbstractEventMetadataBackwardsCompatibilityTest {

    private ObjectMapper objectMapper;

    @Before
    public void setUpObjectMapper() {
        objectMapper = new ObjectMapper();
    }

    @Override
    protected ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    @Override
    protected String getBasePackage() {
        return "be.butskri";
    }

}
