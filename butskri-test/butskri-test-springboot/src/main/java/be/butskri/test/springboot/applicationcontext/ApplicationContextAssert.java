package be.butskri.test.springboot.applicationcontext;

import org.assertj.core.api.Condition;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class ApplicationContextAssert {

    private final ApplicationContext context;

    private ApplicationContextAssert(ApplicationContext context) {
        this.context = context;
    }

    public static ApplicationContextAssert assertThatContext(ApplicationContext context) {
        return new ApplicationContextAssert(context);
    }

    public ApplicationContextAssert containsNoOtherAutoConfigurationsThen(Class<?>... autoconfigurationClasses) {
        String[] autoConfigurationClassNames = Arrays.stream(autoconfigurationClasses)
                .map(Class::getName)
                .collect(Collectors.toList())
                .toArray(new String[0]);
        assertThat(getAutoConfigurationBeanNames()).containsOnly(autoConfigurationClassNames);
        return this;
    }

    public ApplicationContextAssert containsAutoConfigurationOfType(Class<?> type) {
        assertThat(findBeansOfType(type))
                .describedAs("Context does not contain AutoConfiguration bean of type %s", type.getName())
                .hasSize(1);
        return this;
    }

    public ApplicationContextAssert doesNotContainsAutoConfigurationOfType(Class<?> type) {
        assertThat(findBeansOfType(type))
                .describedAs("Context should not contain AutoConfiguration bean of type %s", type.getName())
                .isEmpty();
        return this;
    }

    public ApplicationContextAssert containsBeanWithName(String name) {
        assertThat(context.containsBean(name))
                .describedAs("Context should contain bean with name %s", name)
                .isTrue();
        return this;
    }

    public ApplicationContextAssert doesNotContainBeanWithName(String name) {
        assertThat(context.containsBean(name))
                .describedAs("Context should not contain bean with name %s", name)
                .isFalse();
        return this;
    }

    public ApplicationContextBeansAsserter containsAtLeast(int number) {
        return new ContainsAtLeast(number);
    }

    public ApplicationContextBeansAsserter containsAtMost(int number) {
        return new ContainsAtMost(number);
    }

    public ApplicationContextBeansAsserter containsExactly(int number) {
        return new ContainsExactly(number);
    }

    public AtMostAssert hasAtMost(int maxNumber) {
        return new AtMostAssert(maxNumber);
    }

    private Collection<Object> getAllBeans() {
        return getBeans((bean) -> true);
    }

    private Collection<String> getAutoConfigurationBeanNames() {
        return Arrays.stream(context.getBeanDefinitionNames())
                .filter(this::isAutoConfigurationBean)
                .collect(Collectors.toList());
    }

    private boolean isAutoConfigurationBean(String beanName) {
        if (beanName == null) {
            return false;
        }
        return beanName.endsWith("AutoConfiguration");
    }

    private Collection<Object> getBeans(Predicate<Object> predicate) {
        return Arrays.stream(context.getBeanDefinitionNames())
                .map(beanName -> getBean(beanName))
                .filter(predicate)
                .collect(Collectors.toList());
    }

    private Object getBean(String beanName) {
        return context.getBean(beanName);
    }

    public abstract class ApplicationContextBeansAsserter {

        protected ApplicationContextBeansAsserter() {
        }

        protected abstract void assertBeans(Collection<?> beans, String beansDescription);

        public ApplicationContextAssert beans() {
            assertBeans(findBeansOfType(Object.class), "beans");
            return ApplicationContextAssert.this;
        }

        public ApplicationContextAssert beansOfType(Class<?> type) {
            assertBeans(findBeansOfType(type), "beans of type " + type.getName());
            return ApplicationContextAssert.this;
        }

        public ApplicationContextAssert autoConfigurationBeans() {
            assertBeans(getAutoConfigurationBeanNames(), "AutoConfiguration beans");
            return ApplicationContextAssert.this;
        }
    }

    private Collection<?> findBeansOfType(Class<?> type) {
        return context.getBeansOfType(type).values();
    }

    private class ContainsAtLeast extends ApplicationContextBeansAsserter {
        private int number;

        private ContainsAtLeast(int number) {
            this.number = number;
        }

        @Override
        protected void assertBeans(Collection<?> beans, String beansDescription) {
            if (beans.size() < number) {
                fail("expecting at least %s %s but found %s", number, beansDescription, beans.size());
            }
        }
    }

    private class ContainsAtMost extends ApplicationContextBeansAsserter {
        private int number;

        private ContainsAtMost(int number) {
            this.number = number;
        }

        @Override
        protected void assertBeans(Collection<?> beans, String beansDescription) {
            if (beans.size() > number) {
                fail("expecting at most %s %s but found %s", number, beansDescription, beans.size());
            }
        }
    }

    private class ContainsExactly extends ApplicationContextBeansAsserter {
        private int number;

        private ContainsExactly(int number) {
            this.number = number;
        }

        @Override
        protected void assertBeans(Collection<?> beans, String beansDescription) {
            assertThat(beans)
                    .describedAs("expecting exactly %s %s", number, beansDescription)
                    .hasSize(number);
        }
    }

    public class AtMostAssert {

        private final int maxNumber;

        private AtMostAssert(int maxNumber) {
            this.maxNumber = maxNumber;
        }

        public ApplicationContextAssert beans() {
            return assertAtMost(getAllBeans());
        }

        public ApplicationContextAssert autoConfigurations() {
            return assertAtMost(getAutoConfigurationBeanNames());
        }

        private ApplicationContextAssert assertAtMost(Collection<?> values) {
            assertThat(values)
                    .describedAs("found %s elements", values.size())
                    .areAtMost(maxNumber, alwaysTrue().describedAs("present"));
            return ApplicationContextAssert.this;
        }

    }

    private static Condition<Object> alwaysTrue() {
        return new Condition() {
            @Override
            public boolean matches(Object value) {
                return true;
            }
        };
    }
}
