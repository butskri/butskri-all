package be.butskri.playground.csv;

import org.apache.commons.csv.CSVRecord;

public interface SkipAware {

    void skipped(CSVRecord csvRecord);
}
