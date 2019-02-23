package be.butskri.playground.keng.commons.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ErrorCollector;
import org.reflections.Reflections;

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

}
