package be.butskri.springboot.jpa;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
@ImportAutoConfiguration(DataSourceAutoConfiguration.class)
public class ButskriDatasourceConfiguration {
}
