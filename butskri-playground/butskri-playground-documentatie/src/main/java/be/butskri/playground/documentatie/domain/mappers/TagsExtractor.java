package be.butskri.playground.documentatie.domain.mappers;

import be.butskri.playground.documentatie.domain.configuration.TagsConfiguration;

import java.util.List;
import java.util.stream.Collectors;

public class TagsExtractor {

    private TagsConfiguration tagsConfiguration = new TagsConfiguration();

    List<String> extractTagsFrom(String name) {
        return tagsConfiguration.getConfiguredTags().stream()
                .filter(tag -> tag.matchesWith(name))
                .map(tag -> tag.getTagName())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
}
