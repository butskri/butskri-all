package be.butskri.test.springboot.applicationcontext.autoconfiguretest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

import static be.butskri.test.springboot.applicationcontext.ApplicationContextAssert.assertThatContext;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyVerySimpleApplicationConfiguration.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ApplicationContextAssertTest {

    @Autowired
    private ApplicationContext context;

    @Test
    public void assertContainsAutoConfigurationOfType_success() {
        assertThatContext(context).containsAutoConfigurationOfType(DataSourceAutoConfiguration.class);
    }

    @Test
    public void assertContainsAutoConfigurationOfType_throwsAssertionErrorWhenItDoesNotContainAutoConfiguration() {
        assertThatExceptionOfType(AssertionError.class)
                .isThrownBy(() -> assertThatContext(context).containsAutoConfigurationOfType(PropertyPlaceholderAutoConfiguration.class))
                .withMessageStartingWith("[Context does not contain AutoConfiguration bean of type " + PropertyPlaceholderAutoConfiguration.class.getName());
    }

    @Test
    public void assertDoesNotContainAutoConfigurationOfType_success() {
        assertThatContext(context).doesNotContainsAutoConfigurationOfType(PropertyPlaceholderAutoConfiguration.class);
    }

    @Test
    public void assertDoesNotContainAutoConfigurationOfType_throwsAssertionErrorWhenItContainsAutoConfiguration() {
        assertThatExceptionOfType(AssertionError.class)
                .isThrownBy(() -> assertThatContext(context).doesNotContainsAutoConfigurationOfType(DataSourceAutoConfiguration.class))
                .withMessageStartingWith("[Context should not contain AutoConfiguration bean of type " + DataSourceAutoConfiguration.class.getName());
    }

    @Test
    public void assertNoOtherAutoConfigurationThenDataSourceAutoConfiguration() {
        assertThatContext(context).containsNoOtherAutoConfigurationsThen(DataSourceAutoConfiguration.class);
    }

    @Test
    public void assertContainsExactlyOneAutoConfigurationBeansSucceeds() {
        assertThatContext(context).containsExactly(1).autoConfigurationBeans();
    }

    @Test
    public void assertContainsBeanWithName_successWhenContextContainsBeanWithGivenName() {
        assertThatContext(context).containsBeanWithName("dataSource");
    }

    @Test
    public void assertContainsBeanWithName_throwsAssertionErrorWhenContextDoesNotContainBeanWithGivenName() {
        assertThatExceptionOfType(AssertionError.class)
                .isThrownBy(() -> assertThatContext(context).containsBeanWithName("something"))
                .withMessageStartingWith("[Context should contain bean with name something");
    }

    @Test
    public void assertDoesNotContainBeanWithName_successWhenContextDoesNotContainsBeanWithGivenName() {
        assertThatContext(context).doesNotContainBeanWithName("something");
    }

    @Test
    public void assertDoesNotContainBeanWithName_throwsAssertionErrorWhenContextContainsBeanWithGivenName() {
        assertThatExceptionOfType(AssertionError.class)
                .isThrownBy(() -> assertThatContext(context).doesNotContainBeanWithName("dataSource"))
                .withMessageStartingWith("[Context should not contain bean with name dataSource");
    }

    @Test
    public void assertContainsExactly2AutoConfigurationBeansFails() {
        assertThatExceptionOfType(AssertionError.class)
                .isThrownBy(() -> assertThatContext(context).containsExactly(2).autoConfigurationBeans())
                .withMessageContaining("expecting exactly 2 AutoConfiguration beans")
                .withMessageContaining("Expected size:<2> but was:<1>");
    }

    @Test
    public void assertContainsAtLeastOneAutoConfigurationBeansSucceeds() {
        assertThatContext(context).containsAtLeast(1).autoConfigurationBeans();
    }

    @Test
    public void assertContainsAtLeast2AutoConfigurationBeansFails() {
        assertThatExceptionOfType(AssertionError.class)
                .isThrownBy(() -> assertThatContext(context).containsAtLeast(2).autoConfigurationBeans())
                .withMessage("expecting at least 2 AutoConfiguration beans but found 1");
    }

    @Test
    public void assertContainsAtLeastOneBeansSucceeds() {
        assertThatContext(context).containsAtLeast(1).beans();
    }

    @Test
    public void assertContainsAtLeast100BeansFails() {
        assertThatExceptionOfType(AssertionError.class)
                .isThrownBy(() -> assertThatContext(context).containsAtLeast(100).beans())
                .withMessageContaining("expecting at least 100 beans")
                .withMessageContaining("but found");
    }

    @Test
    public void assertContainsAtLeastOneBeansOfTypeSucceeds() {
        assertThatContext(context).containsAtLeast(1).beansOfType(DataSource.class);
    }

    @Test
    public void assertContainsAtLeastTwoBeansOfTypeThrowsExceptionWhenLessBeansFound() {
        assertThatExceptionOfType(AssertionError.class)
                .isThrownBy(() -> assertThatContext(context).containsAtLeast(2).beansOfType(DataSource.class))
                .withMessage("expecting at least 2 beans of type " + DataSource.class.getName() + " but found 1");
    }

    @Test
    public void assertContainsAtMostOneBeansOfTypeSucceeds() {
        assertThatContext(context).containsAtMost(1).beansOfType(DataSource.class);
    }

    @Test
    public void assertContainsAtMostZeroBeanOfTypeThrowsExceptionWhenMoreBeansFound() {
        assertThatExceptionOfType(AssertionError.class)
                .isThrownBy(() -> assertThatContext(context).containsAtMost(0).beansOfType(DataSource.class))
                .withMessageStartingWith("expecting at most 0 beans of type " + DataSource.class.getName() + " but found 1");
    }
}
