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

import static be.butskri.playground.keng.commons.backwardscompatibility.json.assertions.EventMetadataBackwardsCompatibilityAsserter.fileNameFor;
import static be.butskri.playground.keng.commons.backwardscompatibility.json.assertions.ObjectMapperTestConstants.objectMapperForTests;
import static be.butskri.playground.keng.commons.backwardscompatibility.random.RandomizationTestConstants.baseEnhancedRandomBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class EventMetadataBackwardsCompatibilityAsserterTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private File rootFolder;
    private File actualFolder;
    private File expectedFolder;

    @Before
    public void setUpFolders() throws IOException {
        rootFolder = temporaryFolder.newFolder();
        actualFolder = createFolder("actual");
        expectedFolder = createFolder("expected");
    }

    @Test
    public void assertionSucceedsWhenMetadataIsGeneratedAndMatchesPerfectlyWithClass() throws Throwable {
        asserter().assertAnnotationsForEvents(rootFolder, Lists.newArrayList(MyEvent.class, MyIntegrationEvent.class));

        assertFolderIsEmpty(actualFolder);
        assertFolderContainsOnlyMetadataFor(
                expectedFolder,
                MyEvent.class, MyIntegrationEvent.class, SomeDeepPersonalData.class);
    }

    @Test
    public void assertionSucceedsWhenMetadataMatchesPerfectlyWithClasses() throws Throwable {
        setUpExpectedMetadata("MyEventMetadataFullyMatching.metadata", MyEvent.class);

        asserter().assertAnnotationsForEvents(rootFolder, Lists.newArrayList(MyEvent.class));
        assertFolderIsEmpty(actualFolder);
    }

    @Test
    public void assertionSucceedsWhenJsonFormattedDifferentlyButContentsMatches() throws Throwable {
        setUpExpectedMetadata("MyEventMetadataFormattedDifferently.metadata", MyEvent.class);

        asserter().assertAnnotationsForEvents(rootFolder, Lists.newArrayList(MyEvent.class));
        assertFolderIsEmpty(actualFolder);
    }

    @Test
    public void assertionFailsWhenMetadataDoesNotMatch() throws Throwable {
        setUpExpectedMetadata("MyEventMetadataNotMatching.metadata", MyEvent.class);

        try {
            asserter().assertAnnotationsForEvents(rootFolder, Lists.newArrayList(MyEvent.class));
            fail("AssertionError should have been thrown!");
        } catch (ComparisonFailure expected) {
            assertThat(expected.getMessage())
                    .contains(String.format("metadata for %s should remain the same", MyEvent.class));
        }
        assertFolderContainsOnlyMetadataFor(actualFolder, MyEvent.class);
    }

    @Test
    public void assertionFailsWhenNewUnknownClassAndAsserterConfiguredToFailOnMissingExpectedFile() throws Throwable {
        try {
            asserter(backwardsCompatibilityAsserterConfiguration().withFailOnMissingExpectedFile(true))
                    .assertAnnotationsForEvents(rootFolder, Lists.newArrayList(MyIntegrationEvent.class));
            fail("AssertionError should have been thrown!");
        } catch (AssertionError expected) {
            assertThat(expected.getMessage())
                    .contains(
                            String.format("Expected metadata file missing for %s. " +
                                            "Probably you created a new event or added a new @DeepPersonalData field. " +
                                            "You can generate the expected file using failOnMissingExpectedFile=false",
                                    MyIntegrationEvent.class));
        }
    }

    private EventMetadataBackwardsCompatibilityAsserter asserter() {
        return asserter(backwardsCompatibilityAsserterConfiguration());
    }

    private EventMetadataBackwardsCompatibilityAsserter asserter(BackwardsCompatibilityAsserterConfiguration configuration) {
        return new EventMetadataBackwardsCompatibilityAsserter(configuration);
    }

    private BackwardsCompatibilityAsserterConfiguration backwardsCompatibilityAsserterConfiguration() {
        return new BackwardsCompatibilityAsserterConfiguration()
                .withObjectMapper(objectMapperForTests())
                .withEnhancedRandom(enhancedRandomBuilder());
    }

    private EnhancedRandom enhancedRandomBuilder() {
        return baseEnhancedRandomBuilder().build();
    }

    private void assertFolderContainsOnlyMetadataFor(File folder, Class<?>... classes) {
        List<String> expectedFileNames = Arrays.stream(classes)
                .map(EventMetadataBackwardsCompatibilityAsserter::fileNameFor)
                .collect(Collectors.toList());
        assertThat(folder.listFiles()).extracting(File::getName).containsOnlyElementsOf(expectedFileNames);
    }

    private void setUpExpectedMetadata(String resourcePath, Class<?> clazz) throws IOException {
        File file = new File(expectedFolder, fileNameFor(clazz));
        if (file.createNewFile()) {
            InputStream resource = getClass()
                    .getResourceAsStream("/backwardscompatibilitytests/metadata/" + resourcePath);
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