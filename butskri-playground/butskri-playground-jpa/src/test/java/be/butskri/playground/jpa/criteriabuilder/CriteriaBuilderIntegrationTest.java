package be.butskri.playground.jpa.criteriabuilder;

import be.butskri.playground.jpa.SpringJpaConfiguration;
import be.butskri.playground.jpa.entitities.MySimpleEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;

@Rollback
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringJpaConfiguration.class)
public class CriteriaBuilderIntegrationTest {

    @Autowired
    private EntityManager entityManager;

    @Test
    public void queryWhereMyStringInVarArgValues() {
        MySimpleEntity entity1 = setUpRandomEntity();
        MySimpleEntity entity2 = setUpRandomEntity();
        setUpRandomEntity();
        setUpRandomEntity();
        entityManager.flush();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<MySimpleEntity> query = criteriaBuilder.createQuery(MySimpleEntity.class);
        Root<MySimpleEntity> root = query.from(MySimpleEntity.class);
        query.where(root.<String>get("myString").in(entity1.getMyString(), entity2.getMyString()));
        List<MySimpleEntity> found = entityManager.createQuery(query).getResultList();

        assertThat(found).containsOnly(entity1, entity2);
    }

    private MySimpleEntity setUpRandomEntity() {
        MySimpleEntity entity = new MySimpleEntity(randomUUID(), "my_" + randomUUID());
        entityManager.persist(entity);
        return entity;
    }

}