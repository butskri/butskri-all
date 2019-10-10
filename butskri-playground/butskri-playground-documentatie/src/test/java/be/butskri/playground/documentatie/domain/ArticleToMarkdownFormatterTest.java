package be.butskri.playground.documentatie.domain;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ArticleToMarkdownFormatterTest {

    private static final int DEFAULT_INTEREST = 80;
    private static final Integer DEFAULT_SCORE = null;
    private static final Status DEFAULT_STATUS = Status.TODO;

    private ArticleToMarkdownFormatter formatter = new ArticleToMarkdownFormatter();
    private TagsExtractor tagsExtractor = new TagsExtractor();

    @Test
    public void doPrintArticleDocumentationLine() {
        printNewArticleLineFor("https://www.google.com");
    }

    @Before
    public void configureTagsExtractor() {
        tagsExtractor
                .configureSimpleTag("architecture")
                .configureSimpleTag("authentication")
                .configureSimpleTag("cloud")
                .configureSimpleTag("docker")
                .configureSimpleTag("event-driven")
                .configureSimpleTag("javascript")
                .configureSimpleTag("jpa")
                .configureSimpleTag("kubernetes")
                .configureSimpleTag("linux")
                .configureSimpleTag("metrics")
                .configureSimpleTag("monitoring")
                .configureSimpleTag("performance")
                .configureSimpleTag("security")
                .configureSimpleTag("spring")
                .configureSimpleTag("swagger")
                .configureSimpleTag("testing")
                .configureTag("authentication", "login")
                .configureTag("authentication", "open id")
                .configureTag("authentication", "open-id")
                .configureTag("containerization", "docker")
                .configureTag("event-sourcing", "event store")
                .configureTag("event-sourcing", "event sourcing")
                .configureTag("event-sourcing", "event-sourcing")
                .configureTag("java", "java ")
                .configureTag("javascript", "js")
                .configureTag("microservices", "microservice")
                .configureTag("open-id", "open id")
                .configureTag("open-id", "open-id")
                .configureTag("patterns", "pattern")
                .configureTag("security", "open id")
                .configureTag("security", "open-id")
                .configureTag("spring-boot", "spring", "boot")
                .configureTag("spring-mvc", "spring mvc")
                .configureTag("tools", "tool")
                .configureTag("transactions", "transaction")
        ;
    }

    private void printNewArticleLineFor(String link) {
        String formatted = formatter.format(newArticle(link));
        System.out.println(formatted);
    }

    private Article newArticle(String link) {
        String name = resolveArticleName(link);
        return Article.builder()
                .withId(UUID.randomUUID())
                .withDate(LocalDate.now())
                .withLink(link)
                .withName(name)
                .withInterest(DEFAULT_INTEREST)
                .withScore(DEFAULT_SCORE)
                .withStatus(DEFAULT_STATUS)
                .withTags(tagsExtractor.extractTagsFrom(name))
                .build();
    }

    private String resolveArticleName(String link) {
        String html = new RestTemplate().getForObject(link, String.class);
        return extractFromHtml(html, "h1");
    }

    private String extractFromHtml(String html, String tag) {
        try {
            org.jsoup.nodes.Document document = Jsoup.parse(html);
            Element element = document.selectFirst(tag);
            return element.text().trim();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static class TagsExtractor {

        private List<ConfiguredTag> configuredTags = new ArrayList<>();

        List<String> extractTagsFrom(String name) {
            return configuredTags.stream()
                    .filter(tag -> tag.matchesWith(name))
                    .map(tag -> tag.tagName)
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());
        }

        TagsExtractor configureTag(String tag, String... words) {
            configuredTags.add(new ConfiguredTag(tag, words));
            return this;
        }

        TagsExtractor configureSimpleTag(String tag) {
            return configureTag(tag, tag);
        }
    }

    private static class ConfiguredTag {
        private String tagName;
        private List<String> words;

        ConfiguredTag(String tagName, String... words) {
            this.tagName = tagName;
            this.words = Arrays.asList(words);
        }

        boolean matchesWith(String name) {
            return words.stream()
                    .allMatch(word -> name.toLowerCase().contains(word));
        }
    }
}