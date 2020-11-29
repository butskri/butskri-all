package be.butskri.playground.images;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class FileFilters {

    public static List<File> imageFilesIn(File folder) {
        List<File> result = new ArrayList<>();
        filterFiles(folder, thatAreDirectory())
                .forEach(subfolder -> result.addAll(imageFilesIn(subfolder)));
        filterFiles(folder, thatAreImages())
                .forEach(result::add);

        return result;
    }

    private static Stream<File> filterFiles(File folder, FileFilter filter) {
        File[] result = folder.listFiles(filter);
        if (result == null) {
            return Stream.empty();
        }
        return Arrays.stream(result);
    }

    private static FileFilter thatAreImages() {
        return fileMatchingPattern("(?)SIMG.*\\.JPG");
    }

    public static FileFilter thatAreDirectory() {
        return file -> file.isDirectory();
    }

    public static FileFilter fileMatchingPattern(String regex) {
        Pattern pattern = Pattern.compile(regex);
        return file -> pattern.matcher(file.getName()).matches();
    }

}
