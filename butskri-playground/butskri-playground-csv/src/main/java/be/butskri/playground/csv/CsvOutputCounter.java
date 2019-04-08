package be.butskri.playground.csv;

import org.apache.commons.csv.CSVRecord;

public class CsvOutputCounter implements CsvOutput, SkipAware {

    private int counter;
    private int skipped;

    public static CsvOutputCounter counter() {
        return new CsvOutputCounter();
    }

    private CsvOutputCounter() {
    }

    @Override
    public void start() {
        this.counter = 0;
        this.skipped = 0;
    }

    @Override
    public void output(CSVRecord csvRecord) {
        counter++;
    }

    @Override
    public void finish() {
        System.out.println("Number of records skipped   : " + skipped);
        System.out.println("Number of records processed : " + counter);
    }

    @Override
    public void skipped(CSVRecord csvRecord) {
        skipped++;
    }

    public int getCounter() {
        return counter;
    }

    public int getSkipped() {
        return skipped;
    }
}
