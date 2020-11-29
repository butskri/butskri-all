package be.butskri.playground.images;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;

public class ImageFile implements Comparable<ImageFile> {

    private File file;
    private LocalDateTime realDateTaken;

    public ImageFile(File file) {
        this.file = file;
    }

    public String getInfo() {
        return String.format("%s %s %s %s", getFileName(), getDateTaken(), getRealDateTaken(), file);
    }

    public String getFileName() {
        return file.getName();
    }

    @Override
    public int compareTo(ImageFile other) {
        return this.getFileName().compareTo(other.getFileName());
    }

    protected void calculateRealDateTaken(LocalDateTime relativeDateTimeTaken, LocalDateTime actualRealTime) {
        Duration duration = Duration.between(relativeDateTimeTaken, getDateTaken());
        this.realDateTaken = actualRealTime.plus(duration);
    }

    public void setRealDateTaken(LocalDateTime realDateTaken) {
        this.realDateTaken = realDateTaken;
    }

    public LocalDateTime getRealDateTaken() {
        return realDateTaken;
    }

    public File getFile() {
        return file;
    }

    public LocalDateTime getDateTaken() {
        try {
            Metadata metadata = JpegMetadataReader.readMetadata(file);

            Directory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);

            Date date = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL, TimeZone.getTimeZone(ZoneId.systemDefault()));
            return date.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
        } catch (JpegProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
