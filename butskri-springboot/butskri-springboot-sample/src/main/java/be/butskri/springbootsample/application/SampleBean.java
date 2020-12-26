package be.butskri.springbootsample.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SampleBean {
    private String value;

    public SampleBean(@Value("${some-property}") String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
