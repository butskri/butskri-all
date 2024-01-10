package be.butskri.playground.jpa.entitities;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;

@Component
public class MySimpleEntityRepository {

    private final EntityManager entityManager;

    public MySimpleEntityRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


}
