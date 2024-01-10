package be.butskri.test.backwardscompatibility.annotated;

import jakarta.persistence.Entity;

@Entity
public class SomeEntity {
    private SomeEntityField someEntityField;

    protected SomeEntity() {
        // necessary for JPA
    }

    public SomeEntity(SomeEntityField someEntityField) {
        this.someEntityField = someEntityField;
    }

    public SomeEntityField getSomeEntityField() {
        return someEntityField;
    }
}
