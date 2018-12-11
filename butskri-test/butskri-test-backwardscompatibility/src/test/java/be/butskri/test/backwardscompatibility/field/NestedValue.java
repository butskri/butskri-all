package be.butskri.test.backwardscompatibility.field;

public class NestedValue {

    private NestedNestedValue value;

    public NestedValue(NestedNestedValue value) {
        this.value = value;
    }

    public NestedNestedValue getValue() {
        return value;
    }
}
