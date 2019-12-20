package be.butskri.projectcleaner;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CleanProject {

    private final File folder;
    private boolean removeEmptyFolders = false;
    private Predicate<File> predicateForFindingFoldersToBeLeftUntouched = file -> false;
    private Predicate<File> predicateForFindingFoldersToCompletelyRemove = file -> false;
    private Predicate<File> predicateForFilesToBeRemoved = file -> false;

    public static void main(String[] args) {
//        File folder = new File(args[0]);
        File folder = new File("D:\\Temp");

        cleanProjectIn(folder)
                .exceptFoldersWithName(".git")
                .exceptFoldersWithName("src")
                .completelyRemoveFoldersWithName("target")
                .completelyRemoveFoldersWithName("build")
                .completelyRemoveFoldersWithName(".idea")
                .completelyRemoveFoldersWithName(".settings")
                .removingAllFilesWithExtension("class")
                .removingAllFilesWithExtension("iml")
                .removingAllFilesWithName(".project")
                .removingAllFilesWithName(".classpath")
                .removingEmptyFolders()
                .now();

    }

    public static CleanProject cleanProjectIn(File folder) {
        return new CleanProject(folder);
    }

    private CleanProject(File folder) {
        this.folder = folder;
    }

    public CleanProject completelyRemoveFoldersWithName(String foldername) {
        predicateForFindingFoldersToCompletelyRemove = predicateForFindingFoldersToCompletelyRemove.or(predicateFileOrFolderHavingName(foldername));
        return this;
    }

    public CleanProject removingAllFilesWithExtension(String extension) {
        return removingAllFilesMatching(".*\\." + extension);
    }

    public CleanProject removingAllFilesMatching(String pattern) {
        predicateForFilesToBeRemoved = predicateForFilesToBeRemoved.or(fileMatchingPattern(pattern));
        return this;
    }

    private CleanProject removingAllFilesWithName(String filename) {
        predicateForFilesToBeRemoved = predicateForFilesToBeRemoved.or(predicateFileOrFolderHavingName(filename));
        return this;
    }

    private Predicate<File> fileMatchingPattern(String regex) {
        Pattern pattern = Pattern.compile(regex);
        return file -> pattern.matcher(file.getName()).matches();
    }

    public CleanProject removingEmptyFolders() {
        this.removeEmptyFolders = true;
        return this;
    }

    public CleanProject exceptFoldersWithName(String folderName) {
        predicateForFindingFoldersToBeLeftUntouched = predicateForFindingFoldersToBeLeftUntouched.or(predicateFileOrFolderHavingName(folderName));
        return this;
    }

    public void now() {
        cleanUpFolder(folder);
    }

    private void cleanUpFolder(File map) {
        List<File> filesToRemove = findFilesToRemoveIn(map);
        filesToRemove.forEach(this::removeFile);
        List<File> foldersToRemoveCompletely = findFoldersToCompletelyRemove(map);
        foldersToRemoveCompletely.forEach(this::removeFolderCompletely);
        List<File> foldersToScan = findFoldersToScan(map);
        foldersToScan.forEach(this::cleanUpFolder);
        if (removeEmptyFolders && folderIsEmpty(map)) {
            removeFolder(map);
        }
    }

    private List<File> findFoldersToCompletelyRemove(File map) {
        return findFiles(map, isFolderPredicate().and(this.predicateForFindingFoldersToCompletelyRemove));
    }

    private List<File> findFoldersToScan(File map) {
        return findFiles(map, isFolderPredicate().and(this.predicateForFindingFoldersToBeLeftUntouched.negate()));
    }

    private List<File> findFilesToRemoveIn(File map) {
        return findFiles(map, isFilePredicate().and(this.predicateForFilesToBeRemoved));
    }

    private void removeFolderCompletely(File map) {
        log("removing folder completely %s", map);
        try {
            FileUtils.deleteDirectory(map);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean folderIsEmpty(File map) {
        return map.isDirectory() && map.listFiles().length == 0;
    }

    private void removeFolder(File map) {
        log("removing folder %s", map);
        map.delete();
    }

    private void removeFile(File file) {
        log("removing file %s", file);
        file.delete();
    }

    private void log(String message, Object... args) {
        System.out.println(String.format(message, args));
    }

    private static Predicate<File> predicateFileOrFolderHavingName(String name) {
        return file -> name.equals(file.getName());
    }

    private static List<File> findFiles(File map, Predicate<File> predicate) {
        return Arrays.stream(map.listFiles())
                .filter(predicate)
                .collect(Collectors.toList());
    }

    private static Predicate<File> isFilePredicate() {
        return File::isFile;
    }

    private static Predicate<File> isFolderPredicate() {
        return File::isDirectory;
    }
}
