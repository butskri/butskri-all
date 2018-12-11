package be.butskri.test.backwardscompatibility.subclasses;

public class MarkedClazz1 implements MyMarker {

    private Bla bla;
    private String something;

    public MarkedClazz1(Bla bla, String something) {
        this.bla = bla;
        this.something = something;
    }

    public Bla getBla() {
        return bla;
    }

    public String getSomething() {
        return something;
    }
}
