package be.butskri.test.backwardscompatibility.field;

public class ClassWithGenerifiedValue {
    private GenerifiedValue<NestedValue> generifiedValue;

    public ClassWithGenerifiedValue(GenerifiedValue<NestedValue> generifiedValue) {
        this.generifiedValue = generifiedValue;
    }

    public GenerifiedValue<NestedValue> getGenerifiedValue() {
        return generifiedValue;
    }
}
