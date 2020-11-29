package be.butskri.playground.images;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ImageFileGroup {

    private List<ImageFile> files = new ArrayList<>();

    public ImageFile getFirstImageFile() {
        return files.get(0);
    }

    public ImageFile getLastImageFile() {
        return files.get(files.size() - 1);
    }

    public void add(ImageFile imageFile) {
        if (imageFile.getRealDateTaken() != null) {
            files.forEach(file -> file.calculateRealDateTaken(imageFile.getDateTaken(), imageFile.getRealDateTaken()));
        } else if (!files.isEmpty() && getLastImageFile().getRealDateTaken() != null) {
            imageFile.calculateRealDateTaken(getLastImageFile().getDateTaken(), getLastImageFile().getRealDateTaken());
        }
        files.add(imageFile);
    }

    public String info() {
        ImageFile first = getFirstImageFile();
        ImageFile last = getLastImageFile();
        return String.format("%s - %s\t%s - %s\t%s - %s",
                first.getFileName(), last.getFileName(),
                format(first.getDateTaken()), format(last.getDateTaken()),
                format(first.getRealDateTaken()), format(last.getRealDateTaken()));
    }

    private String format(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return "null               ";
        }
        return localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public Set<File> getAllFolders() {
        return this.files.stream()
                .map(ImageFile::getFile)
                .map(File::getParentFile)
                .collect(Collectors.toSet());
    }
}
