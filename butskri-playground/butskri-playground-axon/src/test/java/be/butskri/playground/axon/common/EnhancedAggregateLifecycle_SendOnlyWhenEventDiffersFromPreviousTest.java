package be.butskri.playground.axon.common;

import be.butskri.playground.axon.example.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static java.util.UUID.randomUUID;

public class EnhancedAggregateLifecycle_SendOnlyWhenEventDiffersFromPreviousTest {

    private static final MySampleAggregateId AGGREGATE_ID = new MySampleAggregateId(randomUUID());
    private static final String STRING1 = "first";
    private static final String STRING2 = "second";

    private FixtureConfiguration<MySampleAggregate> fixture;

    @Before
    public void setUp() {
        fixture = new AggregateTestFixture<>(MySampleAggregate.class);
    }

    @Test
    public void eventIsSentWhenNoEventOfTypeWasSentYet() {
        SomethingHappenedOnMySampleAggregateEvent firstEvent = somethingHappenedEvent(STRING1);
        CreateMySampleAggregateCommand createCommand = new CreateMySampleAggregateCommand(AGGREGATE_ID);

        SomethingElseHappenedOnMySampleAggregateEvent eventOfOtherType = somethingElseHappenedEvent(STRING1);
        MySampleAggregateCommand command = new MySampleAggregateCommand(AGGREGATE_ID, eventOfOtherType);

        fixture.givenCommands(createCommand)
                .andGiven(firstEvent)
                .when(command)
                .expectEvents(eventOfOtherType);
    }

    @Test
    public void eventIsSentWhenDifferentEventOfSameTypeWasSent() {
        SomethingHappenedOnMySampleAggregateEvent firstEvent = somethingHappenedEvent(STRING1);
        CreateMySampleAggregateCommand createCommand = new CreateMySampleAggregateCommand(AGGREGATE_ID);

        SomethingHappenedOnMySampleAggregateEvent eventOfSameTypeWithDifferentValue = somethingHappenedEvent(STRING2);
        MySampleAggregateCommand command = new MySampleAggregateCommand(AGGREGATE_ID, eventOfSameTypeWithDifferentValue);

        fixture.givenCommands(createCommand)
                .andGiven(firstEvent)
                .when(command)
                .expectEvents(eventOfSameTypeWithDifferentValue);
    }

    @Test
    public void eventIsNotSentWhenEqualEventOfSameTypeWasSent() {
        SomethingHappenedOnMySampleAggregateEvent firstEvent = somethingHappenedEvent(STRING1);
        CreateMySampleAggregateCommand createCommand = new CreateMySampleAggregateCommand(AGGREGATE_ID);

        SomethingHappenedOnMySampleAggregateEvent sameEventAsFirst = somethingHappenedEvent(STRING1);
        MySampleAggregateCommand command = new MySampleAggregateCommand(AGGREGATE_ID, sameEventAsFirst);

        fixture.givenCommands(createCommand)
                .andGiven(firstEvent)
                .when(command)
                .expectEvents();
    }

    @Test
    public void eventIsSentWhenEventOfDifferentTypeWasSent() {
        SomethingHappenedOnMySampleAggregateEvent firstEvent = somethingHappenedEvent(STRING1);
        CreateMySampleAggregateCommand createCommand = new CreateMySampleAggregateCommand(AGGREGATE_ID);

        SomethingElseHappenedOnMySampleAggregateEvent eventOfDifferentType = somethingElseHappenedEvent(STRING1);
        MySampleAggregateCommand command = new MySampleAggregateCommand(AGGREGATE_ID, eventOfDifferentType);

        fixture.givenCommands(createCommand)
                .andGiven(firstEvent)
                .when(command)
                .expectEvents(eventOfDifferentType);
    }

    @Test
    public void eventIsNotSentWhenEqualEventOfSameTypeWasSentBeforeEventOfDifferentType() {
        SomethingHappenedOnMySampleAggregateEvent firstEvent = somethingHappenedEvent(STRING1);
        CreateMySampleAggregateCommand createCommand = new CreateMySampleAggregateCommand(AGGREGATE_ID);
        SomethingElseHappenedOnMySampleAggregateEvent eventOfDifferentType = somethingElseHappenedEvent(STRING1);

        SomethingHappenedOnMySampleAggregateEvent sameEventAsFirst = somethingHappenedEvent(STRING1);
        MySampleAggregateCommand command = new MySampleAggregateCommand(AGGREGATE_ID, sameEventAsFirst);

        fixture.givenCommands(createCommand)
                .andGiven(firstEvent, eventOfDifferentType)
                .when(command)
                .expectEvents();
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
