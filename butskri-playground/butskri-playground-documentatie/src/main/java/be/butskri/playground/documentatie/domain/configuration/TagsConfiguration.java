package be.butskri.playground.documentatie.domain.configuration;

import java.util.ArrayList;
import java.util.List;

public class TagsConfiguration {

    private List<ConfiguredTag> configuredTags = new ArrayList<>();

    public TagsConfiguration() {
        configureTag("AI", " AI ", "artificial");
        configureTag("UX", " UX ");
        configureTag("UI", " UI");
        configureTag("CI / CD", " CI ", " CD ", "CI/CD", "continuous integration", "Continuous Delivery");
        configureTag("GUI", " angular ");
        configureSimpleTag("cd");
        configureSimpleTag("Agile");
        configureSimpleTag("Angular");
        configureSimpleTag("Architecture");
        configureSimpleTag("Authentication");
        configureSimpleTag("Cloud");
        configureSimpleTag("Docker");
        configureSimpleTag("Event-driven");
        configureSimpleTag("Javascript");
        configureSimpleTag("JPA");
        configureSimpleTag("Kubernetes");
        configureSimpleTag("Linux");
        configureSimpleTag("Metrics");
        configureSimpleTag("Monitoring");
        configureSimpleTag("Performance");
        configureSimpleTag("Scrum");
        configureSimpleTag("Security");
        configureSimpleTag("Spring");
        configureSimpleTag("Swagger");
        configureSimpleTag("Testing");
        configureTag("Authentication", "login");
        configureTag("Authentication", "open id");
        configureTag("Authentication", "open-id");
        configureTag("containerization", "docker");
        configureTag("Event-sourcing", "event store");
        configureTag("Event-sourcing", "event sourcing");
        configureTag("Event-sourcing", "event-sourcing");
        configureTag("Java", "java ");
        configureTag("Javascript", "js");
        configureTag("Microservices", "microservice");
        configureTag("Open-id", "open id");
        configureTag("Open-id", "open-id");
        configureTag("Patterns", "pattern");
        configureTag("Security", "open id");
        configureTag("Security", "open-id");
        configureTag("Spring-boot", "spring", "boot");
        configureTag("Spring-mvc", "spring mvc");
        configureTag("Tools", "tool");
        configureTag("Transactions", "transaction");
        configureTag("Scrum", "standup");
    }

    public List<ConfiguredTag> getConfiguredTags() {
        return configuredTags;
    }

    private void configureTag(String tag, String... words) {
        configuredTags.add(new ConfiguredTag(tag, words));
    }

    private void configureSimpleTag(String tag) {
        configureTag(tag, tag);
    }

}
