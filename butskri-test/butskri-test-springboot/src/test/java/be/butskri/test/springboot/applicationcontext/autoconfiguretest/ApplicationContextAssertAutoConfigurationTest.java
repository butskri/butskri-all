package be.butskri.test.springboot.applicationcontext.autoconfiguretest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.dao.PersistenceExceptionTranslationAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.http.codec.CodecsAutoConfiguration;
import org.springframework.boot.autoconfigure.info.ProjectInfoAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.jta.JtaAutoConfiguration;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import static be.butskri.test.springboot.applicationcontext.ApplicationContextAssert.assertThatContext;
import static be.butskri.test.springboot.applicationcontext.autoconfiguretest.RegexBuilder.anything;
import static be.butskri.test.springboot.applicationcontext.autoconfiguretest.RegexBuilder.regex;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyAutoConfigurationEnabledApplicationConfiguration.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ApplicationContextAssertAutoConfigurationTest {

    @Autowired
    private ApplicationContext context;

    @Test
    public void assertNoOtherAutoConfigurationThenPresent() {
        assertThatContext(context).containsNoOtherAutoConfigurationsThen(
                ProjectInfoAutoConfiguration.class,
                PropertyPlaceholderAutoConfiguration.class,
                ConfigurationPropertiesAutoConfiguration.class,
                AopAutoConfiguration.class,
                CodecsAutoConfiguration.class,
                JacksonAutoConfiguration.class,
                TaskExecutionAutoConfiguration.class,
                TaskSchedulingAutoConfiguration.class,
                ValidationAutoConfiguration.class,
                // database configuration
                DataSourceAutoConfiguration.class,
                JdbcTemplateAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class,
                JpaRepositoriesAutoConfiguration.class,
                PersistenceExceptionTranslationAutoConfiguration.class,
                DataSourceTransactionManagerAutoConfiguration.class,
                JtaAutoConfiguration.class,
                TransactionAutoConfiguration.class,
                // webservice configuration
                HttpMessageConvertersAutoConfiguration.class,
                RestTemplateAutoConfiguration.class
        );
    }

    @Test
    public void assertNoOtherAutoConfigurationThenDataSourceAutoConfigurationFailsWithCorrectMessage() {
        assertThatThrownBy(
                () -> assertThatContext(context)
                        .containsNoOtherAutoConfigurationsThen(DataSourceAutoConfiguration.class))
                .isInstanceOf(AssertionError.class)
                .hasMessageMatching(
                        regex(anything()
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
