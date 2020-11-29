package be.butskri.playground.images;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class DetermineImageDates {

    private static final Logger LOGGER = Logger.getLogger(DetermineImageDates.class.getName());

    //    private static final File ROOT_DIR = new File("D:\\Todo\\My_Multimedia");
//    private static final File ROOT_DIR = new File("D:\\My_Multimedia");
    private static final File ROOT_DIR_1 = new File("D:\\My_Multimedia");
    private static final File ROOT_DIR_2 = new File("D:\\Todo\\My_Multimedia");
    public static final LocalDateTime LOCALDATE_2020_01_01 = LocalDateTime.of(2020, 1, 1, 0, 0, 0);
    public static final String[] FILE_NAMES_TO_BE_EXCLUDED = {
            "SIMG0296.JPG", "SIMG0318.JPG", "SIMG0426.JPG", "SIMG0588.JPG", "SIMG1762.JPG"
    };
    private final List<File> folders;
    private EstimatedImageDates estimatedImageDates;
    private List<String> filenamesToExclude = new ArrayList<>();
    private Predicate<ImageFile> imagesToBeExcludedPredicate = file -> false;

    private DetermineImageDates(File... folders) {
        this.folders = Arrays.asList(folders);
    }

    public static void main(String[] args) {
        determineImageDatesIn(ROOT_DIR_1, ROOT_DIR_2)
                .using(estimatedImageDates())
//                .excluding("SIMG0431.JPG", "SIMG0444.JPG", "SIMG0935.JPG", "SIMG1252.JPG", "SIMG1503.JPG", "SIMG1810.JPG")
                .excluding(
                        imageFiles()
                                .where(ImageFile::getDateTaken)
                                .isAfter(LOCALDATE_2020_01_01)
                                .or(
                                        imageFiles()
                                                .where(ImageFile::getFileName)
                                                .isIn(FILE_NAMES_TO_BE_EXCLUDED)
                                )
                )
                .now();
    }

    private static ExtensiblePredicate<ImageFile> imageFiles() {
        return ExtensiblePredicate.objectsOfType(ImageFile.class);
    }

    private static EstimatedImageDates estimatedImageDates() {
        return new EstimatedImageDates()
                .withEstimatedImageDate("SIMG0117.JPG", LocalDateTime.of(2003, 4, 6, 12, 0, 0))
                .withEstimatedImageDate("SIMG1616.JPG", LocalDateTime.of(2006, 12, 31, 12, 0, 0))
                .withEstimatedImageDate("SIMG0225.JPG", LocalDateTime.of(2003, 7, 18, 21, 52, 21))
                .withEstimatedImageDate("SIMG0313.JPG", LocalDateTime.of(2003, 8, 15, 11, 30, 0))
                .withEstimatedImageDate("SIMG0314.JPG", LocalDateTime.of(2003, 8, 15, 11, 33, 0))
                .withEstimatedImageDate("SIMG0338.JPG", LocalDateTime.of(2003, 8, 15, 11, 40, 0))
                .withEstimatedImageDate("SIMG0415.JPG", LocalDateTime.of(2003, 10, 4, 7, 40, 0))
                .withEstimatedImageDate("SIMG0527.JPG", LocalDateTime.of(2004, 2, 20, 19, 40, 0))
                .withEstimatedImageDate("SIMG0562.JPG", LocalDateTime.of(2004, 7, 6, 20, 7, 0))
                .withEstimatedImageDate("SIMG0563.JPG", LocalDateTime.of(2004, 7, 6, 20, 8, 0))
                .withEstimatedImageDate("SIMG0601.JPG", LocalDateTime.of(2004, 9, 4, 17, 53, 0))
                .withEstimatedImageDate("SIMG0904.JPG", LocalDateTime.of(2004, 11, 18, 19, 24, 0))
                .withEstimatedImageDate("SIMG0919.JPG", LocalDateTime.of(2005, 1, 4, 21, 45, 0))
                .withEstimatedImageDate("SIMG0972.JPG", LocalDateTime.of(2005, 5, 20, 10, 58, 0))
                .withEstimatedImageDate("SIMG1219.JPG", LocalDateTime.of(2005, 8, 4, 16, 0, 0))
                .withEstimatedImageDate("SIMG1258.JPG", LocalDateTime.of(2005, 9, 29, 10, 0, 0))
                .withEstimatedImageDate("SIMG1293.JPG", LocalDateTime.of(2005, 10, 1, 11, 20, 0))
                .withEstimatedImageDate("SIMG1616.JPG", LocalDateTime.of(2006, 12, 31, 20, 20, 0))
                .withEstimatedImageDate("SIMG1794.JPG", LocalDateTime.of(2008, 1, 1, 16, 30, 0))
                ;
    }

    private DetermineImageDates excluding(String... filenames) {
        this.filenamesToExclude.addAll(Arrays.asList(filenames));
        return this;
    }

    private DetermineImageDates excluding(Predicate<ImageFile> imagesToBeExcludedPredicate) {
        this.imagesToBeExcludedPredicate = imagesToBeExcludedPredicate;
        return this;
    }

    private DetermineImageDates using(EstimatedImageDates estimatedImageDates) {
        this.estimatedImageDates = estimatedImageDates;
        return this;
    }

    private void now() {
        log("Finding all image files in %s", foldersFormatted());
        List<ImageFile> files = getImageFiles();
        ImageFileGroups groups = ImageFileGroups.createImageFileGroups(files, estimatedImageDates);
        showResults(groups);
        showResults(files);
//        files.stream().forEach(file -> log(" %s", file.getInfo()));
        log("Finished!");
    }

    private void showResults(List<ImageFile> files) {
        log("**********************************************************************************************************");
        List<ImageFolderGroup> folderGroups = ImageFolderGroup.mapToFolderGroups(files);
        folderGroups.stream().forEach(group -> log(group.getInfo()));
    }

    private void showResults(ImageFileGroups groups) {
        groups.getGroups().stream().forEach(this::logGroupInfo);
    }

    private String foldersFormatted() {
        return folders.stream()
                .map(File::getAbsolutePath)
                .collect(Collectors.joining());
    }

    private void logGroupInfo(ImageFileGroup group) {
        log(" %s", group.info());
        group.getAllFolders().stream()
                .forEach(folder -> log("   %s", folder.getAbsolutePath()));
    }

    private List<ImageFile> getImageFiles() {
        return folders.stream()
                .map(this::getImageFilesIn)
                .flatMap(List::stream)
                .filter(file -> !imagesToBeExcludedPredicate.test(file))
                .sorted()
                .collect(Collectors.toList());
    }

    private List<ImageFile> getImageFilesIn(File folder) {
        return FileFilters.imageFilesIn(folder)
                .stream()
                .map(ImageFile::new)
                .collect(Collectors.toList());

    }

    private void log(String format, Object... args) {
        System.out.println(String.format(format, args));
//        LOGGER.info(String.format(format, args));
    }

    private static DetermineImageDates determineImageDatesIn(File... folders) {
        return new DetermineImageDates(folders);
    }
}
