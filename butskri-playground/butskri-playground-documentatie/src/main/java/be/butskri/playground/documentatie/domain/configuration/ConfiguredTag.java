package be.butskri.playground.documentatie.domain.configuration;

import java.util.Arrays;
import java.util.List;

public class ConfiguredTag {
    private String tagName;
    private List<String> words;

    public ConfiguredTag(String tagName, String... words) {
        this.tagName = tagName;
        this.words = Arrays.asList(words);
    }

    public boolean matchesWith(String name) {
        return words.stream()
                .map(String::toLowerCase)
                .allMatch(word -> name.toLowerCase().contains(word));
    }

    public String getTagName() {
        return tagName;
    }
}
