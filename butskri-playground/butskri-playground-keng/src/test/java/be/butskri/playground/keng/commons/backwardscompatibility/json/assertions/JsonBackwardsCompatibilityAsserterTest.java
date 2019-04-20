package be.butskri.playground.keng.commons.backwardscompatibility.json.assertions;

import io.github.benas.randombeans.api.EnhancedRandom;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.ComparisonFailure;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static be.butskri.playground.keng.commons.backwardscompatibility.json.assertions.JsonBackwardsCompatibilityAsserter.fileNameFor;
import static be.butskri.playground.keng.commons.backwardscompatibility.json.assertions.ObjectMapperTestConstants.objectMapperForTests;
import static be.butskri.playground.keng.commons.backwardscompatibility.random.RandomizationTestConstants.baseEnhancedRandomBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class JsonBackwardsCompatibilityAsserterTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private JsonBackwardsCompatibilityAsserter asserter;
    private File rootFolder;
    private File actualFolder;
    private File expectedFolder;

    @Before
    public void setUpFolders() throws IOException {
        rootFolder = temporaryFolder.newFolder();
        actualFolder = createFolder("actual");
        expectedFolder = createFolder("expected");
    }

    @Before
    public void setUp() {
        asserter = new JsonBackwardsCompatibilityAsserter(objectMapperForTests(), enhancedRandomBuilder());
    }

    @Test
    public void assertionSucceedsWhenJsonIsGeneratedAndMatchesPerfectlyWithClass() throws Throwable {
        asserter.assertJsonIsBackwardsCompatibleFor(rootFolder, Lists.newArrayList(MyEvent.class, MyIntegrationEvent.class));

        assertFolderIsEmpty(actualFolder);
        assertFolderContainsOnlyJsonFor(expectedFolder, MyEvent.class, MyIntegrationEvent.class);
    }

    @Test
    public void assertionSucceedsWhenJsonMatchesPerfectlyWithClasses() throws Throwable {
        setUpExpectedJson("MyEventFullyMatching.json", MyEvent.class);
        setUpExpectedJson("MyIntegrationEventFullyMatching.json", MyIntegrationEvent.class);

        asserter.assertJsonIsBackwardsCompatibleFor(rootFolder, Lists.newArrayList(MyEvent.class, MyIntegrationEvent.class));
        assertFolderIsEmpty(actualFolder);
    }

    @Test
    public void assertionFailsWhenJsonContainsAdditionalField() throws Throwable {
        setUpExpectedJson("MyEventTooManyFields.json", MyEvent.class);

        try {
            asserter.assertJsonIsBackwardsCompatibleFor(rootFolder, Lists.newArrayList(MyEvent.class));
            fail("exception should have bean thrown");
        } catch (ComparisonFailure expected) {
            assertThat(expected.getMessage())
                    .contains("json for clazz class be.butskri.playground.keng.commons.backwardscompatibility.json.assertions.MyEvent not deserialized/serialized correctly");
            assertThat(expected.getExpected())
                    .contains("hahaha")
                    .contains("superfluousValue");
            assertThat(expected.getActual())
                    .contains("hahaha")
                    .doesNotContain("superfluousValue");
        }
        assertFolderContainsOnlyJsonFor(actualFolder, MyEvent.class);
    }

    @Test
    public void assertionFailsWhenObjectContainsAdditionalField() throws Throwable {
        setUpExpectedJson("MyEventNotAllFields.json", MyEvent.class);

        try {
            asserter.assertJsonIsBackwardsCompatibleFor(rootFolder, Lists.newArrayList(MyEvent.class));
            fail("exception should have been thrown!");
        } catch (AssertionError expected) {
            assertThat(expected.getMessage())
                    .contains(String.format("object of type %s should not have null values", MyEvent.class));
        }
        assertFolderContainsOnlyJsonFor(actualFolder, MyEvent.class);
    }

    @Test
    public void assertionFailsWhenNewUnknownClassAndAsserterConfiguredToFailOnNewBeanGeneration() {
        // TODO implement this
    }

    private EnhancedRandom enhancedRandomBuilder() {
        return baseEnhancedRandomBuilder().build();
    }

    private void assertFolderContainsOnlyJsonFor(File folder, Class<?>... classes) {
        List<String> expectedFileNames = Arrays.stream(classes)
                .map(JsonBackwardsCompatibilityAsserter::fileNameFor)
                .collect(Collectors.toList());
        assertThat(folder.listFiles()).extracting(File::getName).containsOnlyElementsOf(expectedFileNames);
    }

    private void setUpExpectedJson(String resourcePath, Class<?> clazz) throws IOException {
        File file = new File(expectedFolder, fileNameFor(clazz));
        if (file.createNewFile()) {
            InputStream resource = getClass().getResourceAsStream("/backwardscompatibilitytests/" + resourcePath);
            FileUtils.write(file, IOUtils.toString(resource, "UTF-8"), "UTF-8");
        }
    }

    private void assertFolderIsEmpty(File folder) {
        assertThat(folder.listFiles()).isEmpty();
    }

    private File createFolder(String name) {
        File file = new File(rootFolder, name);
        file.mkdirs();
        return file;
    }
}