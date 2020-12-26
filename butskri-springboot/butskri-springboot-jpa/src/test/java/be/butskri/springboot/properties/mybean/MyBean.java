package be.butskri.springboot.properties.mybean;

public class MyBean {

    private String someValue;

    public MyBean(String someValue) {
        this.someValue = someValue;
    }

    public String getSomeValue() {
        return someValue;
    }
}
