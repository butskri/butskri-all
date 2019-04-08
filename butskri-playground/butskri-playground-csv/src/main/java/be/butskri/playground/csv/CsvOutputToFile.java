package be.butskri.playground.csv;

import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;

public class CsvOutputToFile implements CsvOutput {

    private final File file;
    private final Charset charset;
    private int column = 0;
    private PrintWriter printWriter;

    public CsvOutputToFile(File file, Charset charset) {
        this.file = file;
        this.charset = charset;
    }

    public static CsvOutputToFile toFile(File file, Charset charset) {
        return new CsvOutputToFile(file, charset);
    }

    public CsvOutputToFile extractingColumn(int column) {
        this.column = column;
        return this;
    }

    @Override
    public void start() throws IOException {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(file);
        printWriter = new PrintWriter(fileWriter);
    }

    @Override
    public void output(CSVRecord csvRecord) {
        printWriter.println(csvRecord.get(column));
    }

    @Override
    public void finish() throws IOException {
        printWriter.close();
        this.printWriter = null;
    }
}
