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
        printNewArticleLineFor("https://dzone.com/articles/deploying-spring-boot-microservice-to-docker-a-qui");
        printNewArticleLineFor("https://dzone.com/articles/what-is-bill-of-materials-bom-in-spring-boot");
        printNewArticleLineFor("https://dzone.com/articles/improving-reliability-with-alibaba-cloud-message-s");
        printNewArticleLineFor("https://dzone.com/articles/google-cloud-vision-with-spring-boot");
        printNewArticleLineFor("https://dzone.com/articles/mitigating-deployment-risk-in-microservice-archite");
        printNewArticleLineFor("https://dzone.com/articles/moving-towards-a-standard-operating-model-for-kube");
        printNewArticleLineFor("https://dzone.com/articles/java-persistence-done-right");
        printNewArticleLineFor("https://axoniq.io/blog-overview/eventstore");
        printNewArticleLineFor("https://dzone.com/articles/concurrency-and-locking-with-jpa-everything-you-ne");
        printNewArticleLineFor("https://dzone.com/articles/my-advice-junior-developers");
        printNewArticleLineFor("https://dzone.com/articles/kubernetes-vs-openshift-what-is-the-difference");
        printNewArticleLineFor("https://dzone.com/articles/adding-swagger-to-spring-boot");
        printNewArticleLineFor("https://dzone.com/articles/hexagonal-architecture-in-java");
        printNewArticleLineFor("https://dzone.com/articles/spring-boot-where-do-the-default-metrics-come-from");
        printNewArticleLineFor("https://dzone.com/articles/java-simpledateformat-is-not-simple");
        printNewArticleLineFor("https://dzone.com/articles/5-free-courses-to-learn-linux-commands-in-depth");
        printNewArticleLineFor("https://dzone.com/articles/how-devs-can-improve-security-part-1");
        printNewArticleLineFor("https://dzone.com/articles/3-javascript-array-methods-every-developer-should");
        printNewArticleLineFor("https://dzone.com/articles/open-id-connect-authentication-with-oauth20-author");
        printNewArticleLineFor("https://dzone.com/articles/add-login-to-your-spring-boot-app-in-10-mins");
        printNewArticleLineFor("https://dzone.com/articles/four-most-used-rest-api-authentication-methods");
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
                .configureSimpleTag("spring")
                .configureSimpleTag("security")
                .configureSimpleTag("javascript")
                .configureSimpleTag("swagger")
                .configureTag("event-sourcing", "event store")
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