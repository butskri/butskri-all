package be.butskri.playground.keng.commons.test.metadata;

import be.butskri.playground.keng.commons.events.Event;
import be.butskri.playground.keng.commons.test.AbstractJsonTest;
import org.apache.commons.io.FileUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public abstract class AbstractEventMetadataBackwardsCompatibilityTest extends AbstractJsonTest {

    private File resultBaseFolder = new File("src/test/resources/backwardscompatibility/metadata");

    @Test
    public void eventAnnotationsShouldBeBackwardsCompatible() {
        clearDirectory(new File(resultBaseFolder, "actual"));
        Collection<Class<?>> eventsToBeChecked = findAllNonAbstractSubclassesOf(Event.class);
        eventMetadataBackwardsCompatibilityAsserter().assertAnnotationsForEvents(resultBaseFolder, eventsToBeChecked);
    }

    private EventMetadataBackwardsCompatibilityAsserter eventMetadataBackwardsCompatibilityAsserter() {
        return new EventMetadataBackwardsCompatibilityAsserter(getObjectMapper());
    }

    private void clearDirectory(File directory) {
        try {
            FileUtils.cleanDirectory(directory);
        } catch (IOException e) {
            Assertions.fail(String.format("Could not clean directory %s", directory), e);
        }
    }

}
