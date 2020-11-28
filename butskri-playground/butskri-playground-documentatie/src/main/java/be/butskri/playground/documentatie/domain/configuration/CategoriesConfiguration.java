package be.butskri.playground.documentatie.domain.configuration;

import org.apache.logging.log4j.util.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class CategoriesConfiguration {

    private List<ConfiguredCategory> configuredCategories = new ArrayList<>();

    public CategoriesConfiguration() {
        configureCategory(1, "AI", "Artificial", " AI ");
        configureCategory(1, "Testing", "test");
        configureCategory(1, "GUI - Angular", "angular");
        configureCategory(1, "Containerization - Docker", "container", "docker");
        configureSimpleCategory(1, "API Design");
        configureSimpleCategory(1, "Agile");
        configureSimpleCategory(1, "Algorithms");
        configureSimpleCategory(2, "Architecture");
        configureSimpleCategory(1, "Aspects");
        configureSimpleCategory(1, "Axon");
        configureSimpleCategory(1, "Books");
        configureSimpleCategory(1, "CQRS");
        configureSimpleCategory(1, "Captcha");
        configureSimpleCategory(1, "Career");
        configureSimpleCategory(1, "Charts");
        configureSimpleCategory(1, "Cloud");
        configureSimpleCategory(1, "Containerization");
        configureSimpleCategory(1, "Contracts");
        configureSimpleCategory(1, "Debugging");
        configureSimpleCategory(1, "Decision making");
        configureSimpleCategory(1, "Design");
        configureSimpleCategory(1, "Design patterns");
        configureSimpleCategory(1, "Development - Full Stack");
        configureSimpleCategory(1, "Devops");
        configureSimpleCategory(1, "Devops - Monitoring");
        configureSimpleCategory(1, "Devops - dashboard");
        configureSimpleCategory(1, "Document-generation");
        configureSimpleCategory(1, "Elasticsearch");
        configureSimpleCategory(1, "Estimations");
        configureSimpleCategory(1, "Event-driven");
        configureSimpleCategory(1, "Functional programming");
        configureSimpleCategory(1, "GUI");
        configureSimpleCategory(1, "GUI - Angular");
        configureSimpleCategory(1, "GUI - charts");
        configureSimpleCategory(1, "GUI Testing");
        configureSimpleCategory(1, "Gamification");
        configureSimpleCategory(1, "General - Tools");
        configureSimpleCategory(1, "Git");
        configureSimpleCategory(1, "Groovy");
        configureSimpleCategory(1, "JDK");
        configureSimpleCategory(1, "JPA");
        configureSimpleCategory(1, "Java");
        configureSimpleCategory(1, "Javascript / Typescript");
        configureSimpleCategory(1, "Jira");
        configureSimpleCategory(1, "Leadership");
        configureSimpleCategory(1, "Linux");
        configureSimpleCategory(1, "Logging");
        configureSimpleCategory(1, "Microservices");
        configureSimpleCategory(1, "Networks");
        configureSimpleCategory(1, "Performance");
        configureSimpleCategory(1, "Presentations");
        configureSimpleCategory(1, "Protocols - RSocket");
        configureSimpleCategory(1, "Queues");
        configureSimpleCategory(1, "REST");
        configureSimpleCategory(1, "Reactive programming");
        configureSimpleCategory(1, "SQL");
        configureSimpleCategory(1, "Security");
        configureSimpleCategory(1, "Security - Authentication");
        configureSimpleCategory(1, "Security - Encryption");
        configureSimpleCategory(1, "Security - JWT");
        configureSimpleCategory(1, "Security - SSL");
        configureSimpleCategory(1, "Security - TSL");
        configureSimpleCategory(1, "Simple code");
        configureSimpleCategory(1, "Softskills");
        configureSimpleCategory(1, "Software quality");
        configureSimpleCategory(1, "Spring");
        configureSimpleCategory(1, "Spring MVC");
        configureSimpleCategory(1, "Spring REST");
        configureSimpleCategory(1, "Spring Security");
        configureSimpleCategory(1, "Spring Web Mvc");
        configureSimpleCategory(1, "Spring boot");
        configureSimpleCategory(1, "Tools");
        configureSimpleCategory(1, "Transactions");
    }

    public List<ConfiguredCategory> getConfiguredCategories() {
        return configuredCategories;
    }

    private void configureCategory(int weight, String category, Object... words) {
        configuredCategories.add(new ConfiguredCategory(weight, category, words));
    }

    private void configureSimpleCategory(int weight, String category) {
        configureCategory(weight, category, wordsFromCategory(category));
    }

    private Object[] wordsFromCategory(String category) {
        return Stream.of(category.split(" "))
                .filter(value -> !Strings.isEmpty(value) && !"-".equals(value))
                .map(String::toLowerCase)
                .toArray();
    }


}
