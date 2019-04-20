package be.butskri.playground.keng.commons.backwardscompatibility.json.assertions;

import be.butskri.playground.keng.commons.events.Event;

public class MyEvent extends Event {

    private String myString;
    private int myInt;

    public String getMyString() {
        return myString;
    }

    public void setMyString(String myString) {
        this.myString = myString;
    }

    public int getMyInt() {
        return myInt;
    }

    public void setMyInt(int myInt) {
        this.myInt = myInt;
    }
}
