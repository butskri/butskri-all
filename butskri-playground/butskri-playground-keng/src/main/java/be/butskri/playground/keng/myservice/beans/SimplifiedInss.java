package be.butskri.playground.keng.myservice.beans;

import java.util.regex.Pattern;

public class SimplifiedInss {

    private static final Pattern PATTERN = Pattern.compile("[0-9]{11}");

    private String value;

    private SimplifiedInss() {
    }

    public SimplifiedInss(String value) {
        if (!PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("invalid inss: " + value);
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
