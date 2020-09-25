package be.butskri.playground.documentatie.domain;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.util.Strings;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArticleCsvLineGenerator {

    private static final File OUTPUT_CSV_FILE = new File("butskri-playground/butskri-playground-documentatie/src/main/resources/output.csv");
    private static Logger LOGGER = LoggerFactory.getLogger(ArticleCsvLineGenerator.class);

    private static final int DEFAULT_INTEREST = 80;
    private static final Integer DEFAULT_SCORE = null;
    private static final Status DEFAULT_STATUS = Status.TODO;

    private ArticleToMarkdownFormatter formatter = new ArticleToMarkdownFormatter();
    private TagsExtractor tagsExtractor = new TagsExtractor();
    private CategoryExtractor categoryExtractor = new CategoryExtractor();

    public static void main(String[] args) throws IOException {
        List<String> urls = readUrls();

        new ArticleCsvLineGenerator().printArticleLines(urls);
    }

    private List<Article> toArticles(List<String> urls) {
        return urls.stream()
                .map(this::newArticle)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private static List<String> readUrls() {
        InputStream inputStream = ArticleCsvLineGenerator.class.getResourceAsStream("/urls.txt");
        try {
            return IOUtils.readLines(inputStream, "UTF-8")
                    .stream()
                    .filter(url -> !url.startsWith("#"))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void printArticleLines(List<String> urls) throws IOException {
        configureTagsExtractor();
        configureCategoryExtractor();
        List<Article> articles = toArticles(urls);
        List<String> lines = articles.stream().map(this::toArticleLine).collect(Collectors.toList());
        FileUtils.writeLines(OUTPUT_CSV_FILE, "UTF-8", lines);
    }

    private void configureCategoryExtractor() {
        categoryExtractor
                .configureCategory(1, "AI", "Artificial", " AI ")
                .configureCategory(1, "Testing", "test")
                .configureCategory(1, "GUI - Angular", "angular")
                .configureCategory(1, "Containerization - Docker", "container", "docker")
                .configureSimpleCategory(1, "API Design")
                .configureSimpleCategory(1, "Agile")
                .configureSimpleCategory(1, "Algorithms")
                .configureSimpleCategory(2, "Architecture")
                .configureSimpleCategory(1, "Aspects")
                .configureSimpleCategory(1, "Axon")
                .configureSimpleCategory(1, "Books")
                .configureSimpleCategory(1, "CQRS")
                .configureSimpleCategory(1, "Captcha")
                .configureSimpleCategory(1, "Career")
                .configureSimpleCategory(1, "Charts")
                .configureSimpleCategory(1, "Cloud")
                .configureSimpleCategory(1, "Containerization")
                .configureSimpleCategory(1, "Contracts")
                .configureSimpleCategory(1, "Debugging")
                .configureSimpleCategory(1, "Decision making")
                .configureSimpleCategory(1, "Design")
                .configureSimpleCategory(1, "Design patterns")
                .configureSimpleCategory(1, "Development - Full Stack")
                .configureSimpleCategory(1, "Devops")
                .configureSimpleCategory(1, "Devops - Monitoring")
                .configureSimpleCategory(1, "Devops - dashboard")
                .configureSimpleCategory(1, "Document-generation")
                .configureSimpleCategory(1, "Elasticsearch")
                .configureSimpleCategory(1, "Estimations")
                .configureSimpleCategory(1, "Event-driven")
                .configureSimpleCategory(1, "Functional programming")
                .configureSimpleCategory(1, "GUI")
                .configureSimpleCategory(1, "GUI - Angular")
                .configureSimpleCategory(1, "GUI - charts")
                .configureSimpleCategory(1, "GUI Testing")
                .configureSimpleCategory(1, "Gamification")
                .configureSimpleCategory(1, "General - Tools")
                .configureSimpleCategory(1, "Git")
                .configureSimpleCategory(1, "Groovy")
                .configureSimpleCategory(1, "JDK")
                .configureSimpleCategory(1, "JPA")
                .configureSimpleCategory(1, "Java")
                .configureSimpleCategory(1, "Javascript / Typescript")
                .configureSimpleCategory(1, "Jira")
                .configureSimpleCategory(1, "Leadership")
                .configureSimpleCategory(1, "Linux")
                .configureSimpleCategory(1, "Logging")
                .configureSimpleCategory(1, "Microservices")
                .configureSimpleCategory(1, "Networks")
                .configureSimpleCategory(1, "Performance")
                .configureSimpleCategory(1, "Presentations")
                .configureSimpleCategory(1, "Protocols - RSocket")
                .configureSimpleCategory(1, "Queues")
                .configureSimpleCategory(1, "REST")
                .configureSimpleCategory(1, "Reactive programming")
                .configureSimpleCategory(1, "SQL")
                .configureSimpleCategory(1, "Security")
                .configureSimpleCategory(1, "Security - Authentication")
                .configureSimpleCategory(1, "Security - Encryption")
                .configureSimpleCategory(1, "Security - JWT")
                .configureSimpleCategory(1, "Security - SSL")
                .configureSimpleCategory(1, "Security - TSL")
                .configureSimpleCategory(1, "Simple code")
                .configureSimpleCategory(1, "Softskills")
                .configureSimpleCategory(1, "Software quality")
                .configureSimpleCategory(1, "Spring")
                .configureSimpleCategory(1, "Spring MVC")
                .configureSimpleCategory(1, "Spring REST")
                .configureSimpleCategory(1, "Spring Security")
                .configureSimpleCategory(1, "Spring Web Mvc")
                .configureSimpleCategory(1, "Spring boot")
                .configureSimpleCategory(1, "Tools")
                .configureSimpleCategory(1, "Transactions");
    }

    private void configureTagsExtractor() {
        tagsExtractor
                .configureTag("AI", " AI ", "artificial")
                .configureTag("UX", " UX ")
                .configureTag("UI", " UI")
                .configureTag("CI / CD", " CI ", " CD ", "CI/CD", "continuous integration", "Continuous Delivery")
                .configureSimpleTag("cd")
                .configureSimpleTag("agile")
                .configureSimpleTag("angular")
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
                .configureSimpleTag("scrum")
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
                .configureTag("scrum", "standup")
        ;
    }

    private String toArticleLine(Article article) {
        return formatter.format(article);
    }

    private Optional<Article> newArticle(String link) {
        return resolveArticleName(link)
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

    private Optional<String> resolveArticleName(String link) {
        try {
            String html = new RestTemplate().getForObject(link, String.class);
            return Optional.of(extractFromHtml(html, "h1"));
        } catch (RuntimeException e) {
            LOGGER.debug("error while resolving article name in link {}", link, e);
            LOGGER.error("error while resolving article name in link {}", link);
            return Optional.empty();
        }
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

    private static class CategoryExtractor {

        private List<ConfiguredCategory> configuredCategories = new ArrayList<>();

        CategoryExtractor configureCategory(int weight, String category, Object... words) {
            configuredCategories.add(new ConfiguredCategory(weight, category, words));
            return this;
        }

        CategoryExtractor configureSimpleCategory(int weight, String category) {
            return configureCategory(weight, category, wordsFromCategory(category));
        }

        private Object[] wordsFromCategory(String category) {
            return Stream.of(category.split(" "))
                    .filter(value -> !Strings.isEmpty(value) && !"-".equals(value))
                    .map(String::toLowerCase)
                    .toArray();
        }

        Optional<String> extractCategory(String name) {
            return configuredCategories.stream()
                    .filter(tag -> tag.matchesWith(name))
                    .sorted(comparingCategoriesFor(name))
                    .map(tag -> tag.categoryName)
                    .findFirst();
        }

        private Comparator<? super ConfiguredCategory> comparingCategoriesFor(String name) {
            return Comparator.comparing(cat -> cat.calculateScoreFor(name));
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
                    .map(String::toLowerCase)
                    .allMatch(word -> name.toLowerCase().contains(word));
        }
    }

    private static class ConfiguredCategory {
        private int weight;
        private String categoryName;
        private Set<String> words;

        ConfiguredCategory(int weight, String tagName, Object... words) {
            this.weight = weight;
            this.categoryName = tagName;
            this.words = Arrays.asList(words).stream().map(Object::toString).collect(Collectors.toSet());
        }

        boolean matchesWith(String name) {
            return words.stream()
                    .map(String::toLowerCase)
                    .anyMatch(word -> name.toLowerCase().contains(word));
        }

        Long calculateScoreFor(String name) {
            long score = words.stream().filter(word -> name.toLowerCase().contains(word)).count() * 10 - words.size();
            return score * weight;
        }
    }

}