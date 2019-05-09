package be.kindengezin.groeipakket.backwardscompatibility.json.assertions;

import be.kindengezin.groeipakket.backwardscompatibility.json.JsonBackwardsCompatibilityTestConfiguration;
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

import static be.kindengezin.groeipakket.backwardscompatibility.json.assertions.MetadataBackwardsCompatibilityAsserter.fileNameFor;
import static be.kindengezin.groeipakket.backwardscompatibility.json.assertions.ObjectMapperTestConstants.objectMapperForTests;
import static be.kindengezin.groeipakket.backwardscompatibility.json.random.RandomizationTestConstants.baseEnhancedRandomBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class MetadataBackwardsCompatibilityAsserterTest {

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
    public void assertAnnotationsForEventsSucceedsWhenMetadataIsGeneratedAndMatchesPerfectlyWithClass() throws Throwable {
        asserter().assertAnnotationsForEvents(rootFolder, Lists.newArrayList(MyEvent.class, MyIntegrationEvent.class));

        assertFolderIsEmpty(actualFolder);
        assertFolderContainsOnlyMetadataFor(
                expectedFolder,
                MyEvent.class, MyIntegrationEvent.class);
    }

    @Test
    public void assertAnnotationsForEventsSucceedsWhenMetadataMatchesPerfectlyWithClasses() throws Throwable {
        setUpExpectedMetadata("MyEventMetadataFullyMatching.metadata", MyEvent.class);

        asserter().assertAnnotationsForEvents(rootFolder, Lists.newArrayList(MyEvent.class));
        assertFolderIsEmpty(actualFolder);
    }

    @Test
    public void assertAnnotationsForEventsSucceedsWhenJsonFormattedDifferentlyButContentsMatches() throws Throwable {
        setUpExpectedMetadata("MyEventMetadataFormattedDifferently.metadata", MyEvent.class);

        asserter().assertAnnotationsForEvents(rootFolder, Lists.newArrayList(MyEvent.class));
        assertFolderIsEmpty(actualFolder);
    }

    @Test
    public void assertAnnotationsForEventsFailsWhenMetadataDoesNotMatch() throws Throwable {
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
    public void assertAnnotationsForEventsFailsWhenNewUnknownClassAndAsserterConfiguredToFailOnMissingExpectedFile() throws Throwable {
        try {
            asserter(
                    backwardsCompatibilityAsserterConfiguration()
                            .withFailOnMissingExpectedFileEnabled(true))
                    .assertAnnotationsForEvents(rootFolder, Lists.newArrayList(MyIntegrationEvent.class));
            fail("AssertionError should have been thrown!");
        } catch (AssertionError expected) {
            assertThat(expected.getMessage())
                    .matches(
                            String.format("Metadata file .* missing for %s. " +
                                            "Probably you created a new event or added a new @DeepPersonalData field. " +
                                            "You can generate the expected file using " +
                                            "JsonBackwardsCompatibilityTestConfiguration.withFailOnMissingExpectedFileEnabled\\(false\\)",
                                    MyIntegrationEvent.class));
        }
    }

    private MetadataBackwardsCompatibilityAsserter asserter() {
        return asserter(backwardsCompatibilityAsserterConfiguration());
    }

    private MetadataBackwardsCompatibilityAsserter asserter(JsonBackwardsCompatibilityTestConfiguration configuration) {
        return new MetadataBackwardsCompatibilityAsserter(configuration);
    }

    private JsonBackwardsCompatibilityTestConfiguration backwardsCompatibilityAsserterConfiguration() {
        return new JsonBackwardsCompatibilityTestConfiguration()
                .withObjectMapper(objectMapperForTests())
                .withEnhancedRandom(enhancedRandomBuilder());
    }

    private EnhancedRandom enhancedRandomBuilder() {
        return baseEnhancedRandomBuilder().build();
    }

    private void assertFolderContainsOnlyMetadataFor(File folder, Class<?>... classes) {
        List<String> expectedFileNames = Arrays.stream(classes)
                .map(MetadataBackwardsCompatibilityAsserter::fileNameFor)
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