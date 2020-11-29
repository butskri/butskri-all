package be.butskri.playground.images;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ImageFileGroups {

    private List<ImageFileGroup> groups;

    public static ImageFileGroups createImageFileGroups(Collection<ImageFile> files, EstimatedImageDates estimatedImageDates) {
        Builder result = new Builder(estimatedImageDates);
        files.stream().sorted().forEach(result::addImageFile);
        return result.build();
    }

    private ImageFileGroups(List<ImageFileGroup> groups) {
        this.groups = groups;
    }

    public List<ImageFileGroup> getGroups() {
        return groups;
    }

    private static class Builder {

        private EstimatedImageDates estimatedImageDates;
        private List<ImageFileGroup> groups = new ArrayList<>();
        private ImageFileGroup currentGroup;
        private ImageFile lastImageFile;

        private Builder(EstimatedImageDates estimatedImageDates) {
            this.estimatedImageDates = estimatedImageDates;
        }

        public ImageFileGroups build() {
            return new ImageFileGroups(groups);
        }

        private Builder addImageFile(ImageFile imageFile) {
            prepareImageFile(imageFile);
            if (lastImageFile == null || lastImageFile.getDateTaken().isAfter(imageFile.getDateTaken())) {
                currentGroup = new ImageFileGroup();
                groups.add(currentGroup);
            }
            currentGroup.add(imageFile);
            this.lastImageFile = imageFile;
            return this;
        }

        private void prepareImageFile(ImageFile imageFile) {
            LocalDateTime estimatedTime = estimatedImageDates.getEstimatedTimeFor(imageFile.getFileName());
            if (estimatedTime != null) {
                imageFile.setRealDateTaken(estimatedTime);
            }
        }
    }

//    private static class ImageFileGroupBuilder {
//        private List<ImageFile> files = new ArrayList<>();
//
//
//    }
//
}
