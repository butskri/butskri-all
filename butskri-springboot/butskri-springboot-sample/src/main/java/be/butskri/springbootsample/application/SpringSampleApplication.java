package be.butskri.springbootsample.application;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@ComponentScan
@Configuration
//@EnableAutoConfiguration
public class SpringSampleApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(SpringSampleApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        // TODO: add initializers??
//        springApplication.addInitializers(new ContextIdApplicationContextInitializer());
//        new org.springframework.boot.test.context.ConfigFileApplicationContextInitializer();
        ConfigurableApplicationContext context = springApplication.run(args);
        SampleBean bean = context.getBean(SampleBean.class);
        System.out.println("bean value: " + bean.getValue());
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

            System.out.println("Let's inspect the beans provided by Spring Boot:");

            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName);
            }

        };
    }

}