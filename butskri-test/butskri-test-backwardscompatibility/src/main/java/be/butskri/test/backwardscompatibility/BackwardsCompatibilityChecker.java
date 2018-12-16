package be.butskri.test.backwardscompatibility;

import be.butskri.test.backwardscompatibility.classes.Classes;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

public class BackwardsCompatibilityChecker {

    private static final String UTF_8 = "UTF-8";
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Classes classes;
    private boolean writeToFileOnFailure = true;
    private File resultBaseFolder = new File("src/test/resources");

    public static BackwardsCompatibilityChecker assertBackwardsCompatibilityOf(Classes classes) {
        return new BackwardsCompatibilityChecker(classes);
    }

    private BackwardsCompatibilityChecker(Classes classes) {
        this.classes = classes;
    }

    public BackwardsCompatibilityChecker writingToFileOnFailure(boolean writeToFileOnFailure) {
        this.writeToFileOnFailure = writeToFileOnFailure;
        return this;
    }

    public BackwardsCompatibilityChecker writingToFolderOnFailure(File resultBaseFolder) {
        this.resultBaseFolder = resultBaseFolder;
        return this;
    }

    public void isOk() {
        hashesOfAllClassesRemainedSame();
    }

    private void hashesOfAllClassesRemainedSame() {
        List<HashForClass> hashesForClasses = hashesFor(classesToBeChecked());
        try {
            assertThat(hashesForClasses).containsExactlyElementsOf(expectedHashes());
        } catch (Throwable t) {
            if (writeToFileOnFailure) {
                writeResultsToFile(hashesForClasses);
            }
            throw t;
        }
    }

    private List<HashForClass> hashesFor(Collection<Class> classesToBeChecked) {
        HashBuilder hashBuilder = new HashBuilder();
        return classesToBeChecked.stream()
                .map(clazz -> HashForClass.create(clazz, hashBuilder))
                .sorted()
                .collect(Collectors.toList());
    }

    private Collection<Class> classesToBeChecked() {
        return classes.filteredClasses();
    }

    private void writeResultsToFile(List<HashForClass> actualHashesForClasses) {
        File dir = resultBaseFolder;
        if (dir.mkdirs()) {
            logger.debug("created folder {}", dir);
        }
        File file = new File(dir, actualResourceName());
        List<String> lines = actualHashesForClasses.stream()
                .map(HashForClass::toLine)
                .collect(Collectors.toList());
        try {
            logger.debug("Writing results to file {}", file.getAbsolutePath());
            FileUtils.writeLines(file, lines);
        } catch (IOException e) {
            logger.error("Problem writing result to file {}", file, e);
        }
    }

    private List<? extends HashForClass> expectedHashes() {
        String resourceName = expectedResourceName();
        try (InputStream inputStream = getClass().getResourceAsStream(resourceName)) {
            if (inputStream == null) {
                logger.warn("resource does not exist {}", resourceName);
                return emptyList();
            }
            return loadExpectedHashes(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<? extends HashForClass> loadExpectedHashes(InputStream inputStream) throws IOException {
        List<String> lines = IOUtils.readLines(inputStream, UTF_8);
        return lines.stream()
                .filter(((Predicate<String>) String::isEmpty).negate())
                .map(HashForClass::fromLine)
                .collect(Collectors.toList());
    }

    private String actualResourceName() {
        return resource("actual");
    }

    private String expectedResourceName() {
        return resource("expected");
    }

    private String resource(String type) {
        return "/backwardscompatibility/" + type + "-" + name() + ".txt";
    }

    private String name() {
        return "hashes-" + classes.getName();
    }
}
