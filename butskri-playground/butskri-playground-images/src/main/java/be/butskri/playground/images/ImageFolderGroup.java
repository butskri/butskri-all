package be.butskri.playground.images;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ImageFolderGroup {

    private final File folder;
    private final List<ImageFile> imageFiles;

    public static List<ImageFolderGroup> mapToFolderGroups(List<ImageFile> files) {
        Multimap<File, ImageFile> groups = HashMultimap.create();
        files.stream().forEach(file -> groups.put(file.getFile().getParentFile(), file));
        return groups.keySet()
                .stream()
                .map(folder -> new ImageFolderGroup(folder, groups.get(folder)))
                .sorted((group1, group2) -> group1.getFirstImageName().compareTo(group2.getFirstImageName()))
                .collect(Collectors.toList());
    }

    public String getInfo() {
        ImageFile firstImage = firstImage();
        ImageFile lastImage = lastImage();
        return String.format("%s - %s      %s - %s      %s  %s",
                firstImage.getFileName(), lastImage.getFileName(),
                format(firstImage.getRealDateTaken()), format(lastImage.getRealDateTaken()),
                isLongPeriod() ? "!" : " ",
                folder.getAbsolutePath());
    }

    private String format(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return "????      ";
        }
        return localDateTime.toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    private boolean isLongPeriod() {
        if (firstImage().getRealDateTaken() == null) {
            return true;
        }
        if (lastImage().getRealDateTaken() == null) {
            return true;
        }
        return firstImage().getRealDateTaken().plusDays(15).isBefore(lastImage().getRealDateTaken());
    }

    private ImageFolderGroup(File folder, Collection<ImageFile> imageFiles) {
        this.folder = folder;
        this.imageFiles = new ArrayList<>(imageFiles);
        Collections.sort(this.imageFiles, (file1, file2) -> file1.getFileName().compareTo(file2.getFileName()));
    }

    private String getFirstImageName() {
        return firstImage().getFileName();
    }

    private ImageFile firstImage() {
        return imageFiles.get(0);
    }

    private ImageFile lastImage() {
        return imageFiles.get(imageFiles.size() - 1);
    }

}
