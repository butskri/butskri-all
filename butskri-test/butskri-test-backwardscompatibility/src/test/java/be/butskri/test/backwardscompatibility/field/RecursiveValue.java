package be.butskri.test.backwardscompatibility.field;

public class RecursiveValue {

    private RecursiveValue recursiveValue;

    public RecursiveValue(RecursiveValue recursiveValue) {
        this.recursiveValue = recursiveValue;
    }

    public RecursiveValue getRecursiveValue() {
        return recursiveValue;
    }
}
