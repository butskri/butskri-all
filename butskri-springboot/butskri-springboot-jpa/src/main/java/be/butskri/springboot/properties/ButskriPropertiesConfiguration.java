package be.butskri.springboot.properties;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.context.config.ConfigFileApplicationListener;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;

@Configuration
// TODO check this
@ImportAutoConfiguration({
        ConfigurationPropertiesAutoConfiguration.class,
        PropertyPlaceholderAutoConfiguration.class})
//@PropertySource({"classpath:/application-propertiestest.yml"})
public class ButskriPropertiesConfiguration {

    //    @Bean
    public Environment environment() {
        return new StandardEnvironment();
    }

    @Bean
    public ConfigFileApplicationListener configFileApplicationListener() {
        return new ConfigFileApplicationListener();
    }

    @Bean
    public YamlPropertySourceLoader yamlPropertySourceLoader() {
        return new YamlPropertySourceLoader();
    }
}
