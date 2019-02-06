package be.butskri.playground.jpa.entitities;

import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

@Component
public class MySimpleEntityRepository {

    private final EntityManager entityManager;

    public MySimpleEntityRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


}
