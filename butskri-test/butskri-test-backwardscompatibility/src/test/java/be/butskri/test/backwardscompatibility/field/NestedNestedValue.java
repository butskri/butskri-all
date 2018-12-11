package be.butskri.test.backwardscompatibility.field;

import java.util.Collection;

public class NestedNestedValue {

    private Collection<NestedNestedNestedValue> values;

    public NestedNestedValue(Collection<NestedNestedNestedValue> values) {
        this.values = values;
    }

    public Collection<NestedNestedNestedValue> getValues() {
        return values;
    }
}
