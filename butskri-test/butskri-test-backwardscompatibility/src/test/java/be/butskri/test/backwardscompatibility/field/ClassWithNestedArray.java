package be.butskri.test.backwardscompatibility.field;

public class ClassWithNestedArray {

    private NestedArrayValue[] values;

    public ClassWithNestedArray(NestedArrayValue[] values) {
        this.values = values;
    }

    public NestedArrayValue[] getValues() {
        return values;
    }
}
