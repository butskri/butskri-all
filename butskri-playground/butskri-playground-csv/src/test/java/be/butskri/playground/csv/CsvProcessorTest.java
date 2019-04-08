package be.butskri.playground.csv;

import org.apache.commons.csv.CSVFormat;
import org.junit.Test;

import static be.butskri.playground.csv.CsvOutputCounter.counter;
import static org.assertj.core.api.Assertions.assertThat;

public class CsvProcessorTest {

    @Test
    public void outputtingExcludingHeaderRow() {
        CsvOutputCounter counter = counter();
        CSVFormat csvFormat = CSVFormat.newFormat(';');
        CsvProcessor.load(getClass().getResourceAsStream("sampleWithHeaders.csv"), csvFormat)
                .excludingHeaderRow()
                .usingFilter(CsvRecordFilters.columnValueIn(0, "Romelu Lukaku", "Eden Hazard", "Marc Wilmots"))
                .outputting(counter);

        assertThat(counter.getSkipped()).isEqualTo(2);
        assertThat(counter.getCounter()).isEqualTo(3);
    }

    @Test
    public void outputtingNotExcludingHeaderRow() {
        CsvOutputCounter counter = counter();
        CSVFormat csvFormat = CSVFormat.newFormat(';');
        CsvProcessor.load(getClass().getResourceAsStream("sampleWithHeaders.csv"), csvFormat)
                .usingFilter(CsvRecordFilters.columnValueIn(0, "Romelu Lukaku", "Eden Hazard", "Marc Wilmots"))
                .outputting(counter);

        assertThat(counter.getSkipped()).isEqualTo(3);
        assertThat(counter.getCounter()).isEqualTo(3);
    }

}