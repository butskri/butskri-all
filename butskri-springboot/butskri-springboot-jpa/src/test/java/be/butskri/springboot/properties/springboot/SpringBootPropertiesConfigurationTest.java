package be.butskri.springboot.properties.springboot;

import be.butskri.springboot.properties.mybean.MyBean;
import be.butskri.springboot.properties.mybean.MyBeanTestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static be.butskri.test.springboot.applicationcontext.ApplicationContextAssert.assertThatContext;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("propertiestest")
@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = {MyBeanTestConfiguration.class},
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class SpringBootPropertiesConfigurationTest {

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
