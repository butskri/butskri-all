package be.butskri.test.backwardscompatibility;

import be.butskri.test.backwardscompatibility.classes.AnnotatedClasses;
import be.butskri.test.backwardscompatibility.classes.Classes;
import be.butskri.test.backwardscompatibility.subclasses.MyMarker;
import jakarta.persistence.Entity;
import org.junit.Ignore;
import org.junit.Test;

import static be.butskri.test.backwardscompatibility.BackwardsCompatibilityChecker.assertBackwardsCompatibilityOf;
import static be.butskri.test.backwardscompatibility.classes.AnnotatedClasses.classesAnnotatedWith;
import static be.butskri.test.backwardscompatibility.classes.Classes.fieldTypesOf;
import static be.butskri.test.backwardscompatibility.classes.Classes.subclassesOf;

public class BackwardsCompatibilityTest {

    // TODO nagaan of dit nog nuttig is
    @Ignore
    @Test
    public void myMarkerClassesShouldBeBackwardsCompatible() {
        assertBackwardsCompatibilityOf(subclassesOf(MyMarker.class)).isOk();
    }

    // TODO nagaan of dit nog nuttig is
    @Ignore
    @Test
    public void fieldTypesOfMyMarkerClassesShouldBeBackwardsCompatible() {
        assertBackwardsCompatibilityOf(relevantFieldTypesIn(subclassesOf(MyMarker.class))).isOk();
    }

    // TODO nagaan of dit nog nuttig is
    @Ignore
    @Test
    public void entitiesShouldBeBackwardsCompatible() {
        assertBackwardsCompatibilityOf(entities()).isOk();
    }

    // TODO nagaan of dit nog nuttig is
    @Ignore
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
