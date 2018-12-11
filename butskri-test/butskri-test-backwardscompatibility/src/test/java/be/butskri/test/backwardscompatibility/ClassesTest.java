package be.butskri.test.backwardscompatibility;

import be.butskri.test.backwardscompatibility.field.*;
import com.google.common.collect.Sets;
import org.junit.Test;

import java.util.Collection;

import static be.butskri.test.backwardscompatibility.Classes.classes;
import static org.assertj.core.api.Assertions.assertThat;

public class ClassesTest {

    @Test
    public void fieldTypes_returnsTypesOfNestedClasses() {
        Classes found = Classes.fieldTypesOf(classes(NestedValue.class));

        assertThat(found.getAllClasses())
                .containsOnly(
                        NestedNestedValue.class,
                        Collection.class,
                        NestedNestedNestedValue.class);
    }

    @Test
    public void fieldTypes_canExcludeStandardJavaClasses() {
        Classes found = Classes.fieldTypesOf(classes(NestedValue.class)).excludingStandardJavaClasses();

        assertThat(found.getAllClasses())
                .containsOnly(
                        NestedNestedValue.class,
                        NestedNestedNestedValue.class);
    }

    @Test
    public void fieldTypes_worksWithGenerics() {
        Classes found = Classes.fieldTypesOf(classes(ClassWithGenerifiedValue.class));

        assertThat(found.getAllClasses())
                .containsOnly(
                        GenerifiedValue.class,
                        NestedValue.class,
                        NestedNestedValue.class,
                        Collection.class,
                        NestedNestedNestedValue.class);
    }

    @Test
    public void fieldTypes_canHandleRecursion() {
        Classes found = Classes.fieldTypesOf(classes(RecursiveValue.class));

        assertThat(found.getAllClasses())
                .containsOnly(RecursiveValue.class);
    }

    @Test
    public void fieldTypes_combinesAllKindOfNestedTypes() {
        Classes found = Classes.fieldTypesOf(classes(SomeType.class));

        assertThat(found.getAllClasses())
                .containsOnly(
                        Collection.class,
                        NestedValue.class,
                        NestedNestedValue.class,
                        NestedNestedNestedValue.class,
                        RecursiveValue.class,
                        WrappedStringValue.class,
                        int.class,
                        String.class,
                        char.class);
    }

    @Test
    public void fieldTypes_excludesAlsoBlocksNestedTypes() {
        Classes found = Classes.fieldTypesOf(classes(SomeType.class)).excluding(Sets.newHashSet(NestedValue.class));

        assertThat(found.getAllClasses())
                .containsOnly(
                        RecursiveValue.class,
                        WrappedStringValue.class,
                        int.class,
                        String.class,
                        char.class);
    }

    @Test
    public void fieldTypes_excludingPrimitives() {
        Classes found = Classes.fieldTypesOf(classes(SomeType.class))
                .excludingPrimitives();

        assertThat(found.getAllClasses())
                .containsOnly(
                        Collection.class,
                        String.class,
                        NestedValue.class,
                        NestedNestedValue.class,
                        NestedNestedNestedValue.class,
                        RecursiveValue.class,
                        WrappedStringValue.class);
    }

    @Test
    public void fieldTypes_returnsArrayValueTypes() {
        Classes found = Classes.fieldTypesOf(classes(ClassWithNestedArray.class));

        assertThat(found.getAllClasses())
                .containsOnly(NestedArrayValue.class);
    }
}