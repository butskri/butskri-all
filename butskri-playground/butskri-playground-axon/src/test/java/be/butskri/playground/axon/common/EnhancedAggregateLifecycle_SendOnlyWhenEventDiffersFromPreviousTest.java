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
    private static final String SOMEONE = "someone";
    private static final String SOMEONE_ELSE = "someoneElse";
    private static final String STRING1 = "first";
    private static final String STRING2 = "second";

    private FixtureConfiguration<MySampleAggregate> fixture;

    @Before
    public void setUp() {
        fixture = new AggregateTestFixture<>(MySampleAggregate.class);
    }

    @Test
    public void eventIsSentWhenNoEventOfTypeWasSentYet() {
        SomethingHappenedEvent firstEvent = somethingHappenedEvent(STRING1);
        CreateMySampleAggregateCommand createCommand = new CreateMySampleAggregateCommand(AGGREGATE_ID);

        SomethingElseHappenedEvent eventOfOtherType = somethingElseHappenedEvent(STRING1);
        MySampleAggregateCommand command = new MySampleAggregateCommand(AGGREGATE_ID, eventOfOtherType);

        fixture.givenCommands(createCommand)
                .andGiven(firstEvent)
                .when(command)
                .expectEvents(eventOfOtherType);
    }

    @Test
    public void eventIsSentWhenDifferentEventOfSameTypeWasSent() {
        SomethingHappenedEvent firstEvent = somethingHappenedEvent(STRING1);
        CreateMySampleAggregateCommand createCommand = new CreateMySampleAggregateCommand(AGGREGATE_ID);

        SomethingHappenedEvent eventOfSameTypeWithDifferentValue = somethingHappenedEvent(STRING2);
        MySampleAggregateCommand command = new MySampleAggregateCommand(AGGREGATE_ID, eventOfSameTypeWithDifferentValue);

        fixture.givenCommands(createCommand)
                .andGiven(firstEvent)
                .when(command)
                .expectEvents(eventOfSameTypeWithDifferentValue);
    }

    @Test
    public void eventIsNotSentWhenEqualEventOfSameTypeWasSent() {
        SomethingHappenedEvent firstEvent = somethingHappenedEvent(STRING1);
        CreateMySampleAggregateCommand createCommand = new CreateMySampleAggregateCommand(AGGREGATE_ID);

        SomethingHappenedEvent sameEventAsFirst = somethingHappenedEvent(STRING1);
        MySampleAggregateCommand command = new MySampleAggregateCommand(AGGREGATE_ID, sameEventAsFirst);

        fixture.givenCommands(createCommand)
                .andGiven(firstEvent)
                .when(command)
                .expectEvents();
    }

    @Test
    public void eventIsSentWhenEventOfDifferentTypeWasSent() {
        SomethingHappenedEvent firstEvent = somethingHappenedEvent(STRING1);
        CreateMySampleAggregateCommand createCommand = new CreateMySampleAggregateCommand(AGGREGATE_ID);

        SomethingElseHappenedEvent eventOfDifferentType = somethingElseHappenedEvent(STRING1);
        MySampleAggregateCommand command = new MySampleAggregateCommand(AGGREGATE_ID, eventOfDifferentType);

        fixture.givenCommands(createCommand)
                .andGiven(firstEvent)
                .when(command)
                .expectEvents(eventOfDifferentType);
    }

    @Test
    public void eventIsNotSentWhenEqualEventOfSameTypeWasSentBeforeEventOfDifferentType() {
        SomethingHappenedEvent firstEvent = somethingHappenedEvent(STRING1);
        CreateMySampleAggregateCommand createCommand = new CreateMySampleAggregateCommand(AGGREGATE_ID);
        SomethingElseHappenedEvent eventOfDifferentType = somethingElseHappenedEvent(STRING1);

        SomethingHappenedEvent sameEventAsFirst = somethingHappenedEvent(STRING1);
        MySampleAggregateCommand command = new MySampleAggregateCommand(AGGREGATE_ID, sameEventAsFirst);

        fixture.givenCommands(createCommand)
                .andGiven(firstEvent, eventOfDifferentType)
                .when(command)
                .expectEvents();
    }

    @Test
    public void eventIsSentWhenSameThingHappenedToSomeoneElse() {
        SomethingHappenedToSomeoneEvent firstEvent = somethingHappenedToSomeoneEvent(SOMEONE_ELSE, STRING1);
        CreateMySampleAggregateCommand createCommand = new CreateMySampleAggregateCommand(AGGREGATE_ID);

        SomethingHappenedToSomeoneEvent somethingHappenedToSomeoneElse = somethingHappenedToSomeoneEvent(SOMEONE, STRING1);
        MySampleAggregateCommand command = new MySampleAggregateCommand(AGGREGATE_ID, somethingHappenedToSomeoneElse);

        fixture.givenCommands(createCommand)
                .andGiven(firstEvent)
                .when(command)
                .expectEvents(somethingHappenedToSomeoneElse);
    }

    @Test
    public void eventIsNotSentWhenSameThingHappenedToSamePersonWithSomethingHappeningToSomeoneElseInBetween() {
        SomethingHappenedToSomeoneEvent firstEvent = somethingHappenedToSomeoneEvent(SOMEONE, STRING1);
        SomethingHappenedToSomeoneEvent somethingHappenedToSomeoneElse = somethingHappenedToSomeoneEvent(SOMEONE_ELSE, STRING1);
        CreateMySampleAggregateCommand createCommand = new CreateMySampleAggregateCommand(AGGREGATE_ID);

        SomethingHappenedToSomeoneEvent sameThingHappenedToSomeoneAgain = somethingHappenedToSomeoneEvent(SOMEONE, STRING1);
        MySampleAggregateCommand command = new MySampleAggregateCommand(AGGREGATE_ID, sameThingHappenedToSomeoneAgain);

        fixture.givenCommands(createCommand)
                .andGiven(firstEvent, somethingHappenedToSomeoneElse)
                .when(command)
                .expectEvents();
    }

    @Test
    public void eventIsSentWhenAnotherThingHappenedToSamePersonWithSomethingHappeningToSomeoneElseInBetween() {
        CreateMySampleAggregateCommand createCommand = new CreateMySampleAggregateCommand(AGGREGATE_ID);
        SomethingHappenedToSomeoneEvent firstEvent = somethingHappenedToSomeoneEvent(SOMEONE, STRING1);
        SomethingHappenedToSomeoneEvent somethingHappenedToSomeoneElse = somethingHappenedToSomeoneEvent(SOMEONE_ELSE, STRING1);

        SomethingHappenedToSomeoneEvent anotherThingHappenedToSomeone = somethingHappenedToSomeoneEvent(SOMEONE, STRING2);
        MySampleAggregateCommand command = new MySampleAggregateCommand(AGGREGATE_ID, anotherThingHappenedToSomeone);

        fixture.givenCommands(createCommand)
                .andGiven(firstEvent, somethingHappenedToSomeoneElse)
                .when(command)
                .expectEvents(anotherThingHappenedToSomeone);
    }

    @Test
    public void givenSomethingWasSetForSomeoneElse_whenSomethingIsSetForSomeone_thenEventIsSent() {
        CreateMySampleAggregateCommand createCommand = new CreateMySampleAggregateCommand(AGGREGATE_ID);
        SomethingHasBeenSetForSomeoneEvent somethingHasBeenSetForSomeoneElse = somethingHasBeenSetForSomeoneEvent(SOMEONE_ELSE, STRING1);

        SomethingHasBeenSetForSomeoneEvent somethingHasBeenSetForSomeone = somethingHasBeenSetForSomeoneEvent(SOMEONE, STRING1);
        MySampleAggregateCommand command = new MySampleAggregateCommand(AGGREGATE_ID, somethingHasBeenSetForSomeone);

        fixture.givenCommands(createCommand)
                .andGiven(somethingHasBeenSetForSomeoneElse)
                .when(command)
                .expectEvents(somethingHasBeenSetForSomeone);
    }

    @Test
    public void givenSomethingWasSetForSomeoneAndThenItWasUnsetForSamePerson_whenSomethingIsSetAgainWithSameValueForSamePerson_thenEventIsSent() {
        CreateMySampleAggregateCommand createCommand = new CreateMySampleAggregateCommand(AGGREGATE_ID);
        SomethingHasBeenSetForSomeoneEvent somethingHasBeenSetForSomeone = somethingHasBeenSetForSomeoneEvent(SOMEONE, STRING1);
        SomethingHasBeenUnsetForSomeoneEvent somethingHasBeenUnsetForSomeone = somethingHasBeenUnsetForSomeoneEvent(SOMEONE);

        SomethingHasBeenSetForSomeoneEvent somethingHasBeenSetForSomeoneAgain = somethingHasBeenSetForSomeoneEvent(SOMEONE, STRING1);
        MySampleAggregateCommand command = new MySampleAggregateCommand(AGGREGATE_ID, somethingHasBeenSetForSomeoneAgain);

        fixture.givenCommands(createCommand)
                .andGiven(somethingHasBeenSetForSomeone, somethingHasBeenUnsetForSomeone)
                .when(command)
                .expectEvents(somethingHasBeenSetForSomeoneAgain);
    }

    @Test
    public void givenSomethingWasSetForSomeoneAndThenSomethingWasUnsetForSomeoneElse_whenSomethingIsSetForSomeoneWithSameValueAsBefore_thenNoEventIsSent() {
        CreateMySampleAggregateCommand createCommand = new CreateMySampleAggregateCommand(AGGREGATE_ID);
        SomethingHasBeenSetForSomeoneEvent somethingHasBeenSetForSomeone = somethingHasBeenSetForSomeoneEvent(SOMEONE, STRING1);
        SomethingHasBeenUnsetForSomeoneEvent somethingHasBeenUnsetForSomeoneElse = somethingHasBeenUnsetForSomeoneEvent(SOMEONE_ELSE);

        SomethingHasBeenSetForSomeoneEvent somethingHasBeenSetForSomeoneAgain = somethingHasBeenSetForSomeoneEvent(SOMEONE, STRING1);
        MySampleAggregateCommand command = new MySampleAggregateCommand(AGGREGATE_ID, somethingHasBeenSetForSomeoneAgain);

        fixture.givenCommands(createCommand)
                .andGiven(somethingHasBeenSetForSomeone, somethingHasBeenUnsetForSomeoneElse)
                .when(command)
                .expectEvents();
    }

    @Test
    public void givenSomethingWasSetForSomeoneAndThenSomethingWasUnsetForSomeoneElse_whenSomethingIsSetForSomeoneWithDifferentValue_thenEventIsSent() {
        CreateMySampleAggregateCommand createCommand = new CreateMySampleAggregateCommand(AGGREGATE_ID);
        SomethingHasBeenSetForSomeoneEvent somethingHasBeenSetForSomeone = somethingHasBeenSetForSomeoneEvent(SOMEONE, STRING1);
        SomethingHasBeenUnsetForSomeoneEvent somethingHasBeenUnsetForSomeoneElse = somethingHasBeenUnsetForSomeoneEvent(SOMEONE_ELSE);

        SomethingHasBeenSetForSomeoneEvent somethingHasBeenSetForSomeoneAgain = somethingHasBeenSetForSomeoneEvent(SOMEONE, STRING1);
        MySampleAggregateCommand command = new MySampleAggregateCommand(AGGREGATE_ID, somethingHasBeenSetForSomeoneAgain);

        fixture.givenCommands(createCommand)
                .andGiven(somethingHasBeenSetForSomeone, somethingHasBeenUnsetForSomeoneElse)
                .when(command)
                .expectEvents();
    }

    @Test
    public void givenSomethingWasUnsetForSomeoneElse_whenSomethingIsUnsetForSomeone_thenEventIsSent() {
        CreateMySampleAggregateCommand createCommand = new CreateMySampleAggregateCommand(AGGREGATE_ID);
        SomethingHasBeenUnsetForSomeoneEvent somethingHasBeenUnsetForSomeoneElse = somethingHasBeenUnsetForSomeoneEvent(SOMEONE_ELSE);

        SomethingHasBeenUnsetForSomeoneEvent somethingHasBeenUnsetForSomeone = somethingHasBeenUnsetForSomeoneEvent(SOMEONE);
        MySampleAggregateCommand command = new MySampleAggregateCommand(AGGREGATE_ID, somethingHasBeenUnsetForSomeone);

        fixture.givenCommands(createCommand)
                .andGiven(somethingHasBeenUnsetForSomeoneElse)
                .when(command)
                .expectEvents(somethingHasBeenUnsetForSomeone);
    }

    @Test
    public void givenSomethingWasUnsetForSomeoneAndThenItWasSetForSamePerson_whenSomethingIsUnsetAgainWithSameValueForSamePerson_thenEventIsSent() {
        CreateMySampleAggregateCommand createCommand = new CreateMySampleAggregateCommand(AGGREGATE_ID);
        SomethingHasBeenUnsetForSomeoneEvent somethingHasBeenUnsetForSomeone = somethingHasBeenUnsetForSomeoneEvent(SOMEONE);
        SomethingHasBeenSetForSomeoneEvent somethingHasBeenSetForSomeone = somethingHasBeenSetForSomeoneEvent(SOMEONE, STRING1);

        SomethingHasBeenUnsetForSomeoneEvent somethingHasBeenUnsetForSomeoneAgain = somethingHasBeenUnsetForSomeoneEvent(SOMEONE);
        MySampleAggregateCommand command = new MySampleAggregateCommand(AGGREGATE_ID, somethingHasBeenUnsetForSomeoneAgain);

        fixture.givenCommands(createCommand)
                .andGiven(somethingHasBeenUnsetForSomeone, somethingHasBeenSetForSomeone)
                .when(command)
                .expectEvents(somethingHasBeenUnsetForSomeoneAgain);
    }

    @Test
    public void givenSomethingWasUnsetForSomeoneAndThenSomethingWasSetForSomeoneElse_whenSomethingIsUnsetForSomeoneAgain_thenNoEventIsSent() {
        CreateMySampleAggregateCommand createCommand = new CreateMySampleAggregateCommand(AGGREGATE_ID);
        SomethingHasBeenUnsetForSomeoneEvent somethingHasBeenUnsetForSomeone = somethingHasBeenUnsetForSomeoneEvent(SOMEONE);
        SomethingHasBeenSetForSomeoneEvent somethingHasBeenSetForSomeoneElse = somethingHasBeenSetForSomeoneEvent(SOMEONE_ELSE, STRING1);

        SomethingHasBeenUnsetForSomeoneEvent somethingHasBeenUnsetForSomeoneAgain = somethingHasBeenUnsetForSomeoneEvent(SOMEONE);
        MySampleAggregateCommand command = new MySampleAggregateCommand(AGGREGATE_ID, somethingHasBeenUnsetForSomeoneAgain);

        fixture.givenCommands(createCommand)
                .andGiven(somethingHasBeenUnsetForSomeone, somethingHasBeenSetForSomeoneElse)
                .when(command)
                .expectEvents();
    }

    private SomethingHappenedEvent somethingHappenedEvent(String data) {
        return new SomethingHappenedEvent(randomMetadata(), AGGREGATE_ID, data);
    }

    private SomethingElseHappenedEvent somethingElseHappenedEvent(String data) {
        return new SomethingElseHappenedEvent(randomMetadata(), AGGREGATE_ID, data);
    }

    private SomethingHappenedToSomeoneEvent somethingHappenedToSomeoneEvent(String someone, String data) {
        return new SomethingHappenedToSomeoneEvent(randomMetadata(), AGGREGATE_ID, someone, data);
    }

    private SomethingHasBeenSetForSomeoneEvent somethingHasBeenSetForSomeoneEvent(String someone, String data) {
        return new SomethingHasBeenSetForSomeoneEvent(randomMetadata(), AGGREGATE_ID, someone, data);
    }

    private SomethingHasBeenUnsetForSomeoneEvent somethingHasBeenUnsetForSomeoneEvent(String someone) {
        return new SomethingHasBeenUnsetForSomeoneEvent(randomMetadata(), AGGREGATE_ID, someone);
    }

    private Map<String, String> randomMetadata() {
        HashMap<String, String> metadata = new HashMap<>();
        metadata.put("random", RandomStringUtils.randomAlphabetic(10));
        return metadata;
    }
}
