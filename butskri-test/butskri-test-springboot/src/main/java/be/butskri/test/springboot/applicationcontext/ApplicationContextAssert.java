package be.butskri.test.springboot.applicationcontext;

import org.assertj.core.api.Condition;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

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
