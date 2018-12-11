package be.butskri.test.backwardscompatibility.subclasses;

public class MarkedClazz2 extends AbstractMarkedClass {

    private Blie blie;
    private int intValue;
    private SomeEnum someEnum;

    public MarkedClazz2(Blie blie, int intValue, SomeEnum someEnum) {
        this.blie = blie;
        this.intValue = intValue;
        this.someEnum = someEnum;
    }

    public Blie getBlie() {
        return blie;
    }

    public int getIntValue() {
        return intValue;
    }

    public SomeEnum getSomeEnum() {
        return someEnum;
    }
}
