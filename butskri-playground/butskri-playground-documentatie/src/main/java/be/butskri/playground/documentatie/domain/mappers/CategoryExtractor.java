package be.butskri.playground.documentatie.domain.mappers;

import be.butskri.playground.documentatie.domain.configuration.CategoriesConfiguration;
import be.butskri.playground.documentatie.domain.configuration.ConfiguredCategory;

import java.util.Comparator;
import java.util.Optional;

class CategoryExtractor {

    private CategoriesConfiguration categoriesConfiguration = new CategoriesConfiguration();

    Optional<String> extractCategory(String name) {
        return categoriesConfiguration.getConfiguredCategories().stream()
                .filter(tag -> tag.matchesWith(name))
                .sorted(comparingCategoriesFor(name))
                .map(tag -> tag.getCategoryName())
                .findFirst();
    }

    private Comparator<? super ConfiguredCategory> comparingCategoriesFor(String name) {
        return Comparator.comparing(cat -> cat.calculateScoreFor(name));
    }
}
