package be.butskri.springboot.application;

import be.butskri.springboot.properties.ButskriPropertiesConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ContextIdApplicationContextInitializer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(ButskriPropertiesConfiguration.class)
public class MyApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(MyApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.addInitializers(new ContextIdApplicationContextInitializer());
        ConfigurableApplicationContext applicationContext = springApplication.run(ButskriPropertiesConfiguration.class, args);
        SomeBean bean = applicationContext.getBean(SomeBean.class);
        System.out.println("found value: " + bean.getValue());
    }

    @Bean
    public ServletWebServerFactory servletWebServerFactory() {
        return new TomcatServletWebServerFactory();
    }
}
