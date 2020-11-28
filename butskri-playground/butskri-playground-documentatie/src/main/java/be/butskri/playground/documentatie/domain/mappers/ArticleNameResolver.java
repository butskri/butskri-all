package be.butskri.playground.documentatie.domain.mappers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

public class ArticleNameResolver {

    private static Logger LOGGER = LoggerFactory.getLogger(ArticleNameResolver.class);

    private RestTemplate restTemplate = new RestTemplate();

    Optional<String> resolveArticleName(String link) {
        try {
            String html = restTemplate.getForObject(link, String.class);
            return Optional.of(extractFromHtml(html, "h1", "title"));
        } catch (RuntimeException e) {
            LOGGER.debug("error while resolving article name in link {}", link, e);
            LOGGER.error("error while resolving article name in link {}", link);
            return Optional.empty();
        }
    }

    private String extractFromHtml(String html, String... tags) {
        Document document = Jsoup.parse(html);
        Element element = findFirstTagPresent(document, tags);
        return element.text().trim();
    }

    private Element findFirstTagPresent(Document document, String... tags) {
        for (int i = 0; i < tags.length; i++) {
            Element element = document.selectFirst(tags[i]);
            if (element != null) {
                return element;
            }
        }
        return null;
    }


}
