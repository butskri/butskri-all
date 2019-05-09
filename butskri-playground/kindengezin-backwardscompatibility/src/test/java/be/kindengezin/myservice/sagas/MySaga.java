package be.kindengezin.myservice.sagas;

import be.kindengezin.myservice.beans.SomeBean;
import be.kindengezin.myservice.beans.SuperBean;
import org.axonframework.spring.stereotype.Saga;

@Saga
public class MySaga {

    private String someField;
    private int someOtherField;
    private SomeBean someBean;
    private SuperBean superBean;
    private transient SomeService someService;

}
