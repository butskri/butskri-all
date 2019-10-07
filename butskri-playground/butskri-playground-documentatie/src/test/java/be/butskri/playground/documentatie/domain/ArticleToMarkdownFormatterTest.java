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
        printNewArticleLineFor("https://dzone.com/articles/kubernetes-vs-openshift-a-detailed-comparison");
        printNewArticleLineFor("https://dzone.com/articles/doing-more-with-swaggger-and-spring");
        printNewArticleLineFor("https://dzone.com/articles/java/spring/spring-mvc-tutorial-1.html");
        printNewArticleLineFor("https://dzone.com/articles/build-beautiful-console-dashboards-with-sampler");
        printNewArticleLineFor("https://dzone.com/articles/spring-boot-transactions-tutorial-understanding-tr");
        printNewArticleLineFor("https://dzone.com/articles/event-driven-microservices-with-spring-boot-and-ac");
        printNewArticleLineFor("https://dzone.com/articles/top-5-online-training-courses-to-learn-frontend-we");
        printNewArticleLineFor("https://dzone.com/articles/top-8-javascript-automation-testing-frameworks-in");
        printNewArticleLineFor("https://dzone.com/articles/microservice-architecture-on-kubernetes");
        printNewArticleLineFor("https://dzone.com/articles/software-quality-the-top-10-metrics-to-build-confi");
        printNewArticleLineFor("https://dzone.com/articles/5-questions-everyones-asking-about-microservices-p");
        printNewArticleLineFor("https://dzone.com/articles/allard-buijze-on-event-driven-microservices-the-se");
        printNewArticleLineFor("https://dzone.com/articles/five-questions-everyone-is-asking-about-microservi");
    }

    @Before
    public void configureTagsExtractor() {
        tagsExtractor
                .configureSimpleTag("architecture")
                .configureSimpleTag("authentication")
                .configureSimpleTag("cloud")
                .configureSimpleTag("linux")
                .configureSimpleTag("docker")
                .configureSimpleTag("jpa")
                .configureSimpleTag("kubernetes")
                .configureSimpleTag("metrics")
                .configureSimpleTag("monitoring")
                .configureSimpleTag("spring")
                .configureSimpleTag("security")
                .configureSimpleTag("javascript")
                .configureSimpleTag("swagger")
                .configureSimpleTag("testing")
                .configureSimpleTag("event-driven")
                .configureTag("event-sourcing", "event store")
                .configureTag("transactions", "transaction")
                .configureTag("spring-mvc", "spring mvc")
                .configureTag("open-id", "open id")
                .configureTag("open-id", "open-id")
                .configureTag("authentication", "login")
                .configureTag("authentication", "open id")
                .configureTag("authentication", "open-id")
                .configureTag("security", "open id")
                .configureTag("security", "open-id")
                .configureTag("containerization", "docker")
                .configureTag("microservices", "microservice")
                .configureTag("java", "java ")
                .configureTag("spring-boot", "spring", "boot")
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