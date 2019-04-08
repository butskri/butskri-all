package be.butskri.playground.csv;

import org.apache.commons.csv.CSVRecord;

import java.util.ArrayList;
import java.util.List;

public class CsvOutputCollector implements CsvOutput {

    private int column;
    private List<String> values;

    private CsvOutputCollector(int column) {
        this.column = column;
    }

    public static CsvOutputCollector collecting(int column) {
        return new CsvOutputCollector(column);
    }

    @Override
    public void start() {
        values = new ArrayList<>();
    }

    @Override
    public void output(CSVRecord csvRecord) {
        values.add(csvRecord.get(column));
    }

    public List<String> getValues() {
        return values;
    }
}
