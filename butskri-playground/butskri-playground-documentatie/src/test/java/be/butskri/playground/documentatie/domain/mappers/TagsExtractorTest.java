package be.butskri.playground.documentatie.domain.mappers;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TagsExtractorTest {

    private TagsExtractor extractor = new TagsExtractor();

    @Test
    public void extractTags() {
        assertThat(extractor.extractTagsFrom("7 Best Angular Component Libraries to Use in 2020"))
                .contains("GUI", "Angular");
    }

}