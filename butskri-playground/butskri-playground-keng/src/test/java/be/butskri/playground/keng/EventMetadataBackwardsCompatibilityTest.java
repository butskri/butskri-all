package be.butskri.playground.keng;

import be.butskri.playground.keng.commons.test.metadata.AbstractEventMetadataBackwardsCompatibilityTest;
import be.butskri.playground.keng.myservice.events.SomeEvent;
import be.butskri.playground.keng.myservice.events.SomeIntegrationEvent;

import java.util.Collection;

import static org.assertj.core.util.Lists.newArrayList;

public class EventMetadataBackwardsCompatibilityTest extends AbstractEventMetadataBackwardsCompatibilityTest {

    @Override
    protected Collection<Class<?>> findEventsToBeChecked() {
        return newArrayList(SomeEvent.class, SomeIntegrationEvent.class);
    }
}
