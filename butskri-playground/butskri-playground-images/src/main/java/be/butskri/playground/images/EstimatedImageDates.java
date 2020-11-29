package be.butskri.playground.images;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class EstimatedImageDates {

    private Map<String, LocalDateTime> estimatedTimes = new HashMap<>();

    public EstimatedImageDates withEstimatedImageDate(String filename, LocalDateTime localDateTime) {
        estimatedTimes.put(filename, localDateTime);
        return this;
    }

    public LocalDateTime getEstimatedTimeFor(String fileName) {
        return estimatedTimes.get(fileName);
    }
}
