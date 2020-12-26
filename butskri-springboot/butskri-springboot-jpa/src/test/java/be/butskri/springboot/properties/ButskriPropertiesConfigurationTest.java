package be.butskri.springboot.properties;

import be.butskri.springboot.properties.mybean.MyBean;
import be.butskri.springboot.properties.mybean.MyBeanTestConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static be.butskri.test.springboot.applicationcontext.ApplicationContextAssert.assertThatContext;
import static org.assertj.core.api.Assertions.assertThat;

//@ExtendWith(SpringExtension.class)
@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {MyBeanTestConfiguration.class, ButskriPropertiesConfiguration.class},
        initializers = ConfigFileApplicationContextInitializer.class)
//@SpringBootTest(classes = {MyBeanTestConfiguration.class, ButskriPropertiesConfiguration.class})
@ActiveProfiles("propertiestest")
//@RunWith(SpringRunner.class)
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = {MyBeanTestConfiguration.class, ButskriPropertiesConfiguration.class})
//@EnableConfigurationProperties
public class ButskriPropertiesConfigurationTest {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private MyBean myBean;
    @Autowired
    private Environment environment;

    // TODO: do something with StandardEnvironment
    // TODO: do something with PropertyMappingContextCustomizer

    @Test
    public void propertyValueIsSet() {
        ConfigFileApplicationContextInitializer configFileApplicationContextInitializer = new ConfigFileApplicationContextInitializer();
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MyBeanTestConfiguration.class);
        configFileApplicationContextInitializer.initialize(applicationContext);
        assertThat(environment.getProperty("someProperty")).isEqualTo("theRightValue");
    }

    @Test
    public void only2AutoConfigurationsShouldBeLoaded() {
        assertThatContext(applicationContext)
                .containsNoOtherAutoConfigurationsThen(
                        ConfigurationPropertiesAutoConfiguration.class,
                        PropertyPlaceholderAutoConfiguration.class);
    }

    @Test
    public void contextShouldContainAtMost5Beans() {
        assertThatContext(applicationContext).containsAtMost(5).beans();
    }

    @Test
    public void propertiesAreInjectedFromYml() {
        assertThat(myBean.getSomeValue()).isEqualTo("theRightValue");
    }
}
