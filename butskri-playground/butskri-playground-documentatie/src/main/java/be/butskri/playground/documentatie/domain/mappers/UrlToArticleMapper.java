package be.butskri.playground.documentatie.domain.mappers;

import be.butskri.playground.documentatie.domain.data.Article;
import be.butskri.playground.documentatie.domain.data.Status;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UrlToArticleMapper {

    private static final int DEFAULT_INTEREST = 80;
    private static final Integer DEFAULT_SCORE = null;
    private static final Status DEFAULT_STATUS = Status.TODO;

    private TagsExtractor tagsExtractor = new TagsExtractor();
    private CategoryExtractor categoryExtractor = new CategoryExtractor();
    private ArticleNameResolver articleNameResolver = new ArticleNameResolver();

    public List<Article> toArticles(List<String> urls) {
        return urls.stream()
                .map(this::newArticle)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private Optional<Article> newArticle(String link) {
        return articleNameResolver.resolveArticleName(link)
                .map(name -> toArticle(link, name));
    }

    private Article toArticle(String link, String name) {
        return Article.builder()
                .withDate(LocalDate.now())
                .withLink(link)
                .withName(name)
                .withCategory(categoryExtractor.extractCategory(name).orElse("???"))
                .withTags(tagsExtractor.extractTagsFrom(name))
                .withInterest(DEFAULT_INTEREST)
                .withScore(DEFAULT_SCORE)
                .withStatus(DEFAULT_STATUS)
                .build();
    }

}
