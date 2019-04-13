package be.butskri.playground.keng.myservice.sagas;

public class SomeService {

    private SomeRepository someRepository;

    private SomeService() {
    }

    SomeService(SomeRepository someRepository) {
        this.someRepository = someRepository;
    }
}
