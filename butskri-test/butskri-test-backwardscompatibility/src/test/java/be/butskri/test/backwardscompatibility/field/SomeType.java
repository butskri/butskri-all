package be.butskri.test.backwardscompatibility.field;

public class SomeType {
    public static StaticField staticField;
    private NestedValue nestedValue;
    private int intValue;
    private RecursiveValue recursiveValue;
    private WrappedStringValue wrappedStringValue;

    public SomeType(NestedValue nestedValue, int intValue, RecursiveValue recursiveValue, WrappedStringValue wrappedStringValue) {
        this.nestedValue = nestedValue;
        this.intValue = intValue;
        this.recursiveValue = recursiveValue;
        this.wrappedStringValue = wrappedStringValue;
    }

    public NestedValue getNestedValue() {
        return nestedValue;
    }

    public int getIntValue() {
        return intValue;
    }

    public RecursiveValue getRecursiveValue() {
        return recursiveValue;
    }

    public WrappedStringValue getWrappedStringValue() {
        return wrappedStringValue;
    }
}
