package be.butskri.test.backwardscompatibility.annotated;

import javax.persistence.Entity;

@Entity
public class SomeEntity {
    private SomeEntityField someEntityField;

    public SomeEntity(SomeEntityField someEntityField) {
        this.someEntityField = someEntityField;
    }

    public SomeEntityField getSomeEntityField() {
        return someEntityField;
    }
}
