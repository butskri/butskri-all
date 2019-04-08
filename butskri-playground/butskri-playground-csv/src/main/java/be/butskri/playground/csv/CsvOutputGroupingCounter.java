package be.butskri.playground.csv;

import org.apache.commons.csv.CSVRecord;

import java.util.HashMap;
import java.util.Map;

public class CsvOutputGroupingCounter implements CsvOutput {

    private int column = 0;
    private Map<String, Integer> counter;

    public static CsvOutputGroupingCounter groupingCounter() {
        return new CsvOutputGroupingCounter();
    }

    private CsvOutputGroupingCounter() {
    }

    public CsvOutputGroupingCounter groupingByColumn(int column) {
        this.column = column;
        return this;
    }

    @Override
    public void start() {
        this.counter = new HashMap<>();
    }

    @Override
    public void output(CSVRecord csvRecord) {
        String key = csvRecord.get(column);
        counter.put(key, counter.getOrDefault(key, 0) + 1);
    }

    @Override
    public void finish() {
        System.out.println("Found values:");
        counter.keySet()
                .stream()
                .sorted()
                .forEach(key -> System.out.println(String.format("%s : %s", key, counter.get(key))));
    }
}
