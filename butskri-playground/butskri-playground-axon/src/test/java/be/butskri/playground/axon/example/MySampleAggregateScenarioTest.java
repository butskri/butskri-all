package be.butskri.playground.axon.example;

import org.apache.commons.lang3.RandomStringUtils;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static java.util.UUID.randomUUID;

public class MySampleAggregateScenarioTest {

    private static final MySampleAggregateId AGGREGATE_ID = new MySampleAggregateId(randomUUID());
    private static final String STRING1 = "first";

    private FixtureConfiguration<MySampleAggregate> fixture;

    @Before
    public void setUp() {
        fixture = new AggregateTestFixture<>(MySampleAggregate.class);
    }

    @Test
    public void createSendsOutCreateEventAndOtherEvents() {
        SomethingHappenedOnMySampleAggregateEvent firstEvent = somethingHappenedEvent(STRING1);
        SomethingElseHappenedOnMySampleAggregateEvent secondEvent = somethingElseHappenedEvent(STRING1);
        CreateMySampleAggregateCommand createCommand = new CreateMySampleAggregateCommand(AGGREGATE_ID, firstEvent, secondEvent);

        fixture.given()
                .when(createCommand)
                .expectEvents(
                        new MySampleAggregateCreatedEvent(AGGREGATE_ID),
                        firstEvent,
                        secondEvent
                );
    }

    private SomethingHappenedOnMySampleAggregateEvent somethingHappenedEvent(String data) {
        return new SomethingHappenedOnMySampleAggregateEvent(randomMetadata(), AGGREGATE_ID, data);
    }

    private SomethingElseHappenedOnMySampleAggregateEvent somethingElseHappenedEvent(String data) {
        return new SomethingElseHappenedOnMySampleAggregateEvent(randomMetadata(), AGGREGATE_ID, data);
    }

    private Map<String, String> randomMetadata() {
        HashMap<String, String> metadata = new HashMap<>();
        metadata.put("random", RandomStringUtils.randomAlphabetic(10));
        return metadata;
    }
}
