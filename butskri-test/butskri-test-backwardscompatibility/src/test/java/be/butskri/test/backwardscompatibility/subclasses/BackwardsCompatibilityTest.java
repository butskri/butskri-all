package be.butskri.test.backwardscompatibility.subclasses;

import be.butskri.test.backwardscompatibility.AnnotatedClasses;
import be.butskri.test.backwardscompatibility.Classes;
import org.junit.Test;

import javax.persistence.Entity;

import static be.butskri.test.backwardscompatibility.AnnotatedClasses.classesAnnotatedWith;
import static be.butskri.test.backwardscompatibility.BackwardsCompatibilityChecker.assertBackwardsCompatibilityOf;
import static be.butskri.test.backwardscompatibility.Classes.fieldTypesOf;
import static be.butskri.test.backwardscompatibility.Classes.subclassesOf;

public class BackwardsCompatibilityTest {

    @Test
    public void myMarkerClassesShouldBeBackwardsCompatible() {
        assertBackwardsCompatibilityOf(subclassesOf(MyMarker.class)).isOk();
    }

    @Test
    public void fieldTypesOfMyMarkerClassesShouldBeBackwardsCompatible() {
        assertBackwardsCompatibilityOf(relevantFieldTypesIn(subclassesOf(MyMarker.class))).isOk();
    }

    @Test
    public void entitiesShouldBeBackwardsCompatible() {
        assertBackwardsCompatibilityOf(entities()).isOk();
    }

    @Test
    public void fieldTypesOfEntitiesShouldBeBackwardsCompatible() {
        assertBackwardsCompatibilityOf(relevantFieldTypesIn(entities())).isOk();
    }

    private AnnotatedClasses entities() {
        return classesAnnotatedWith(Entity.class).inPackage("be");
    }

    private Classes relevantFieldTypesIn(Classes classes) {
        return fieldTypesOf(classes)
                .excludingStandardJavaClasses()
                .excludingPrimitives();
    }
}
