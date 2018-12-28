package be.butskri.test.springboot.applicationcontext.autoconfiguretest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import static be.butskri.test.springboot.applicationcontext.ApplicationContextAssert.assertThatContext;
import static be.butskri.test.springboot.applicationcontext.autoconfiguretest.RegexBuilder.anything;
import static be.butskri.test.springboot.applicationcontext.autoconfiguretest.RegexBuilder.regex;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyAutoConfigurationEnabledApplicationConfiguration.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ApplicationContextAssertAutoConfigureTest {

    @Autowired
    private ApplicationContext context;

    @Test
    public void assertNoOtherAutoConfigurationThenDataSourceAutoConfigurationFailsWithCorrectMessage() {
        assertThatThrownBy(
                () -> assertThatContext(context)
                        .containsNoOtherAutoConfigurationsThen(DataSourceAutoConfiguration.class))
                .isInstanceOf(AssertionError.class)
                .hasMessageMatching(regex(anything()
                        .andThen("Expecting:")
                        .andThen(anything())
                        .andThen("to contain only:")
                        .andThen(anything())
                        .andThen("but the following elements were unexpected")
                        .andThen(anything())));
    }

    @Test
    public void assertAtMost10AutoConfigurationsFailsWithCorrectMessage() {
        assertThatThrownBy(
                () -> assertThatContext(context)
                        .hasAtMost(10).autoConfigurations())
                .isInstanceOf(AssertionError.class)
                .hasMessageMatching(regex(anything()
                        .andThen("Expecting elements")
                        .andThen(anything())
                        .andThen("to be at most 10 times <present>")));
    }

    @Test
    public void assertAtMost10BeansFailsWithCorrectMessage() {
        assertThatThrownBy(
                () -> assertThatContext(context)
                        .hasAtMost(10).beans())
                .isInstanceOf(AssertionError.class)
                .hasMessageMatching(regex(anything()
                        .andThen("Expecting elements")
                        .andThen(anything())
                        .andThen("to be at most 10 times <present>")));
    }

    @Test
    public void assertAtMost100AutoConfigurationsSucceeds() {
        assertThatContext(context)
                .hasAtMost(100).autoConfigurations();
    }

    @Test
    public void assertAtMost120BeansSucceeds() {
        assertThatContext(context)
                .hasAtMost(120).beans();
    }

}
