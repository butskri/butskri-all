package be.butskri.playground.keng.myservice.sagas;

import be.butskri.playground.keng.commons.domain.ProcessManager;
import be.butskri.playground.keng.myservice.beans.SomeBean;
import be.butskri.playground.keng.myservice.beans.SuperBean;

public class MySaga extends ProcessManager {

    private String someField;
    private int someOtherField;
    private SomeBean someBean;
    private SuperBean superBean;
    private transient SomeService someService;

}
