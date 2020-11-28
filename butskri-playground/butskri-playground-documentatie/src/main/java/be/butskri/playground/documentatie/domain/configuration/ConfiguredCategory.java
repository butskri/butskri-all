package be.butskri.playground.documentatie.domain.configuration;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class ConfiguredCategory {
    private int weight;
    private String categoryName;
    private Set<String> words;

    public ConfiguredCategory(int weight, String tagName, Object... words) {
        this.weight = weight;
        this.categoryName = tagName;
        this.words = Arrays.asList(words).stream().map(Object::toString).collect(Collectors.toSet());
    }

    public boolean matchesWith(String name) {
        return words.stream()
                .map(String::toLowerCase)
                .anyMatch(word -> name.toLowerCase().contains(word));
    }

    public Long calculateScoreFor(String name) {
        long score = words.stream().filter(word -> name.toLowerCase().contains(word)).count() * 10 - words.size();
        return score * weight;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
