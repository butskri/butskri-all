package be.butskri.playground.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.nio.charset.Charset;
import java.util.function.Predicate;

public class CsvProcessor {

    private InputStream inputStream;
    private CSVFormat csvFormat;
    private boolean excludeHeaderRow = false;
    private Predicate<CSVRecord> filter = (record) -> true;

    public static CsvProcessor loadFile(File file, CSVFormat csvFormat) throws FileNotFoundException {
        return new CsvProcessor(new FileInputStream(file), csvFormat);
    }

    public static CsvProcessor load(InputStream inputStream, CSVFormat csvFormat) {
        return new CsvProcessor(inputStream, csvFormat);
    }

    private CsvProcessor(InputStream inputStream, CSVFormat csvFormat) {
        this.inputStream = inputStream;
        this.csvFormat = csvFormat;
    }

    public CsvProcessor excludingHeaderRow() {
        this.excludeHeaderRow = true;
        return this;
    }

    public CsvProcessor usingFilter(Predicate<CSVRecord> filter) {
        this.filter = filter;
        return this;
    }

    public CsvProcessor outputting(CsvOutput csvOutput) {
        new ProcessExecutor(csvOutput).process();
        return this;
    }

    private class ProcessExecutor {
        private final CsvOutput csvOutput;
        private boolean headerRowExcluded = false;

        private ProcessExecutor(CsvOutput csvOutput) {
            this.csvOutput = csvOutput;
        }

        public void process() {
            try (InputStream theInputStream = inputStream) {
                CSVParser csvParser = CSVParser.parse(theInputStream, Charset.forName("UTF-8"), csvFormat);
                csvOutput.start();
                csvParser.forEach(csvRecord -> consume(csvRecord));
                csvOutput.finish();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void consume(CSVRecord csvRecord) {
            if (excludeHeaderRow && !headerRowExcluded) {
                headerRowExcluded = true;
                return;
            }
            if (filter.test(csvRecord)) {
                csvOutput.output(csvRecord);
            } else if (isSkipAware(csvOutput)) {
                ((SkipAware) csvOutput).skipped(csvRecord);
            }
        }

        private boolean isSkipAware(CsvOutput csvOutput) {
            return SkipAware.class.isInstance(csvOutput);
        }

    }
}
