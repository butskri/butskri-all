package be.kindengezin.myservice.sagas;

public class SomeService {

    private SomeRepository someRepository;

    private SomeService() {
    }

    SomeService(SomeRepository someRepository) {
        this.someRepository = someRepository;
    }
}
