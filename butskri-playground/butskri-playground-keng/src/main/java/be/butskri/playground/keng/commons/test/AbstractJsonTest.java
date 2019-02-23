package be.butskri.playground.keng.commons.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ErrorCollector;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.stream.Collectors;

public abstract class AbstractJsonTest {

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    protected Reflections reflections;

    @Before
    public void setUpReflections() {
        this.reflections = new Reflections(getBasePackage());
    }

    protected abstract ObjectMapper getObjectMapper();

    protected abstract String getBasePackage();

    protected <T> Collection<Class<?>> findAllNonAbstractSubclassesOf(Class<T> baseClass) {
        return reflections.getSubTypesOf(baseClass)
                .stream()
                .filter(clazz -> !Modifier.isAbstract(clazz.getModifiers()))
                .collect(Collectors.toSet());
    }

}
