package be.butskri.test.backwardscompatibility.subclasses;

public enum SomeEnum {
    VALUE1(new EnumField()),
    VALUE2(new EnumField());

    private EnumField enumField;

    SomeEnum(EnumField enumField) {
        this.enumField = enumField;
    }

    public EnumField getEnumField() {
        return enumField;
    }
}
