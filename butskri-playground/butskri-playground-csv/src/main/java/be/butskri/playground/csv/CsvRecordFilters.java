package be.butskri.playground.csv;

import org.apache.commons.csv.CSVRecord;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class CsvRecordFilters {

    public static Predicate<CSVRecord> columnValueIn(int column, String... possibleValues) {
        Set<String> values = new HashSet<>(Arrays.asList(possibleValues));
        return csvRecord -> values.contains(csvRecord.get(column));
    }

    public static Predicate<CSVRecord> columnValueNotIn(int column, String... possibleValues) {
        return columnValueIn(column, possibleValues).negate();
    }
}
