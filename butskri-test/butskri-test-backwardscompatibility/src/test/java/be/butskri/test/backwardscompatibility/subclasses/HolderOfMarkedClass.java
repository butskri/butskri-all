package be.butskri.test.backwardscompatibility.subclasses;

public class HolderOfMarkedClass {

    private String stringValue;

    public HolderOfMarkedClass(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    static class InnerMarkedClass implements MyMarker {

        private String theValue;
        private int somethingElse;

        public InnerMarkedClass(String theValue, int somethingElse) {
            this.theValue = theValue;
            this.somethingElse = somethingElse;
        }

        public String getTheValue() {
            return theValue;
        }

        public int getSomethingElse() {
            return somethingElse;
        }

        @Override
        public String toString() {
            return theValue + "-" + somethingElse;
        }
    }


}
