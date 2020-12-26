package be.butskri.springboot.properties.mybean;

import be.butskri.springboot.properties.mybean.MyBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.config.ConfigFileApplicationListener;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBeanTestConfiguration {

//    @Bean
//    public MyBean myBean(@Value("${someProperty}") String someValue) {
//        return new MyBean(someValue);
//    }
    @Bean
    public MyBean myBean() {
        return new MyBean("someValue");
    }

    @Bean
    public ConfigFileApplicationContextInitializer configFileApplicationContextInitializer() {
        return new ConfigFileApplicationContextInitializer();
    }

    @Bean
    public ConfigFileApplicationListener configFileApplicationListener() {
        return new ConfigFileApplicationListener();
    }

}
