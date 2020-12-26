package be.butskri.springboot.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SomeBean {
    private String value;

    public SomeBean(@Value("${some-property}") String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
