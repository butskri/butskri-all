package be.butskri.springbootsample.application;

import be.butskri.springboot.properties.ButskriPropertiesConfiguration;
import be.butskri.springboot.properties.mybean.MyBean;
import be.butskri.springboot.properties.mybean.MyBeanTestConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Configuration
@Import({ButskriPropertiesConfiguration.class, MyBeanTestConfiguration.class})
public class MyApplication implements CommandLineRunner {

    @Autowired
    private MyBean myBean;

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ButskriPropertiesConfiguration.class);
        application.setAdditionalProfiles("propertiestest");
//        application.setHeadless(false);
        application.run();
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("myBean: " + myBean.getSomeValue());
    }
}
