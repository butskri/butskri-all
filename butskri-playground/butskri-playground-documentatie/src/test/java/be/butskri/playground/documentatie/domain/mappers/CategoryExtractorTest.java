package be.butskri.playground.documentatie.domain.mappers;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CategoryExtractorTest {

    private CategoryExtractor extractor = new CategoryExtractor();

    @Test
    public void extractCategory() {
        assertThat(extractor.extractCategory("7 Best Angular Component Libraries to Use in 2020")).contains("GUI - Angular");
    }

}