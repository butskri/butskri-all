package be.butskri.test.backwardscompatibility.field;

public class WrappedStringValue {

    private String value;

    public WrappedStringValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
