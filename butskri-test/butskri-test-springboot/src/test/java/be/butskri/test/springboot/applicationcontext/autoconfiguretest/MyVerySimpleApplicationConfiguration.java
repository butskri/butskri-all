package be.butskri.test.springboot.applicationcontext.autoconfiguretest;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
@ImportAutoConfiguration(DataSourceAutoConfiguration.class)
public class MyVerySimpleApplicationConfiguration {

}
