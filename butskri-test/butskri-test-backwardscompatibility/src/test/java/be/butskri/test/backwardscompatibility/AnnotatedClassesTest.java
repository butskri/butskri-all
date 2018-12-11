package be.butskri.test.backwardscompatibility;

import be.butskri.test.backwardscompatibility.annotated.SomeEntity;
import be.butskri.test.backwardscompatibility.annotated.SomeOtherEntity;
import org.junit.Test;

import javax.persistence.Entity;

import static org.assertj.core.api.Assertions.assertThat;

public class AnnotatedClassesTest {

    @Test
    public void canFindClassesByAnnotation() {
        AnnotatedClasses found = AnnotatedClasses.classesAnnotatedWith(Entity.class)
                .inPackage("be");

        assertThat(found.getName()).isEqualTo("classes-annotated-with-Entity");
        assertThat(found.getAllClasses())
                .containsOnly(SomeEntity.class, SomeOtherEntity.class);
    }
}