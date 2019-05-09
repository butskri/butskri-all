package be.kindengezin.myservice.sagas;

import be.butskri.playground.keng.commons.domain.ProcessManager;
import be.kindengezin.myservice.beans.SomeBean;
import be.kindengezin.myservice.beans.SuperBean;

public class MySaga extends ProcessManager {

    private String someField;
    private int someOtherField;
    private SomeBean someBean;
    private SuperBean superBean;
    private transient SomeService someService;

}
