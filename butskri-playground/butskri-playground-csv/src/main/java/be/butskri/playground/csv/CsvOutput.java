package be.butskri.playground.csv;

import org.apache.commons.csv.CSVRecord;

import java.io.IOException;

public interface CsvOutput {

    static CsvOutput noOp() {
        return new CsvOutput() {
        };
    }

    default void start() throws IOException {
    }

    default void output(CSVRecord csvRecord) {
    }

    default void finish() throws IOException {
    }
}
