package be.kindengezin.groeipakket.backwardscompatibility.json.metadata;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class FieldInfoTest {

    @Test
    public void uuidFieldCanBeAnnotatedWithPersonalData() {
        FieldInfo fieldInfo = fieldInfo(SomeTestClass.class, "uuid");

        assertCanBeAnnotatedWithPersonalData(fieldInfo);
    }

    @Test
    public void stringFieldCanBeAnnotatedWithPersonalData() {
        FieldInfo fieldInfo = fieldInfo(SomeTestClass.class, "stringField");

        assertCanBeAnnotatedWithPersonalData(fieldInfo);
    }

    @Test
    public void intFieldCanBeAnnotatedWithPersonalData() {
        FieldInfo fieldInfo = fieldInfo(SomeTestClass.class, "intField");

        assertCanBeAnnotatedWithPersonalData(fieldInfo);
    }

    @Test
    public void integerFieldCanBeAnnotatedWithPersonalData() {
        FieldInfo fieldInfo = fieldInfo(SomeTestClass.class, "integerField");

        assertCanBeAnnotatedWithPersonalData(fieldInfo);
    }

    @Test
    public void arrayOfStringsFieldCanBeAnnotatedWithPersonalData() {
        FieldInfo fieldInfo = fieldInfo(SomeTestClass.class, "arrayOfStrings");

        assertCanBeAnnotatedWithPersonalData(fieldInfo);
    }

    @Test
    public void collectionOfStringsFieldCanBeAnnotatedWithPersonalData() {
        FieldInfo fieldInfo = fieldInfo(SomeTestClass.class, "collectionOfStrings");

        assertCanBeAnnotatedWithPersonalData(fieldInfo);
    }

    @Test
    public void collectionOfStringExtensionsFieldCanBeAnnotatedWithPersonalData() {
        FieldInfo fieldInfo = fieldInfo(SomeTestClass.class, "collectionOfStringExtensions");

        assertCanBeAnnotatedWithPersonalData(fieldInfo);
    }

    @Test
    public void collectionOfStringSupersFieldCanNotBeAnnotatedWithPersonalData() {
        FieldInfo fieldInfo = fieldInfo(SomeTestClass.class, "collectionOfStringSupers");

        assertCanNotBeAnnotatedWithPersonalData(fieldInfo);
    }

    @Test
    public void collectionOfParameterizedExtensionsOfOtherClassesCanNotBeAnnotatedWithPersonalData() {
        FieldInfo fieldInfo = fieldInfo(SomeTestClass.class, "collectionParameterizedOtherClasses");

        assertCanNotBeAnnotatedWithPersonalData(fieldInfo);
    }

    @Test
    public void collectionOfParameterizedExtensionsOfStringsCanBeAnnotatedWithPersonalData() {
        FieldInfo fieldInfo = fieldInfo(SomeTestClass.class, "collectionParameterizedStringExtensions");

        assertCanBeAnnotatedWithPersonalData(fieldInfo);
    }

    @Test
    public void arrayOfIntsCanBeAnnotatedWithPersonalData() {
        FieldInfo fieldInfo = fieldInfo(SomeTestClass.class, "arrayOfInts");

        assertCanBeAnnotatedWithPersonalData(fieldInfo);
    }

    @Test
    public void collectionOfNumbersCanBeAnnotatedWithPersonalData() {
        FieldInfo fieldInfo = fieldInfo(SomeTestClass.class, "collectionOfNumbers");

        assertCanBeAnnotatedWithPersonalData(fieldInfo);
    }

    @Test
    public void collectionOfNumberExtensionsCanBeAnnotatedWithPersonalData() {
        FieldInfo fieldInfo = fieldInfo(SomeTestClass.class, "collectionOfNumberExtensions");

        assertCanBeAnnotatedWithPersonalData(fieldInfo);
    }

    @Test
    public void bigDecimalFieldCanBeAnnotatedWithPersonalData() {
        FieldInfo fieldInfo = fieldInfo(SomeTestClass.class, "bigDecimalField");

        assertCanBeAnnotatedWithPersonalData(fieldInfo);
    }

    @Test
    public void floatFieldCanBeAnnotatedWithPersonalData() {
        FieldInfo fieldInfo = fieldInfo(SomeTestClass.class, "floatField");

        assertCanBeAnnotatedWithPersonalData(fieldInfo);
    }

    @Test
    public void primitiveFloatFieldCanBeAnnotatedWithPersonalData() {
        FieldInfo fieldInfo = fieldInfo(SomeTestClass.class, "primitiveFloatField");

        assertCanBeAnnotatedWithPersonalData(fieldInfo);
    }

    @Test
    public void doubleFieldCanBeAnnotatedWithPersonalData() {
        FieldInfo fieldInfo = fieldInfo(SomeTestClass.class, "doubleField");

        assertCanBeAnnotatedWithPersonalData(fieldInfo);
    }

    @Test
    public void primitiveDoubleFieldCanBeAnnotatedWithPersonalData() {
        FieldInfo fieldInfo = fieldInfo(SomeTestClass.class, "primitiveDoubleField");

        assertCanBeAnnotatedWithPersonalData(fieldInfo);
    }

    @Test
    public void collectionOfParameterizedStringSupersCanNotBeAnnotatedWithPersonalData() {
        FieldInfo fieldInfo = fieldInfo(SomeTestClass.class, "collectionOfParameterizedStringSupers");

        assertCanNotBeAnnotatedWithPersonalData(fieldInfo);
    }

    @Test
    public void collectionOfParameterizedParameterizedStringExtensionsCanBeAnnotatedWithPersonalData() {
        FieldInfo fieldInfo = fieldInfo(SomeTestClass.class, "collectionOfParameterizedParameterizedStringExtensions");

        assertCanBeAnnotatedWithPersonalData(fieldInfo);
    }

    private void assertCanNotBeAnnotatedWithPersonalData(FieldInfo fieldInfo) {
        assertThat(fieldInfo.canBeAnnotatedWithPersonalData()).isFalse();
        assertThat(fieldInfo.canBeAnnotatedWithDeepPersonalData()).isTrue();
    }

    private void assertCanBeAnnotatedWithPersonalData(FieldInfo fieldInfo) {
        assertThat(fieldInfo.canBeAnnotatedWithPersonalData()).isTrue();
        assertThat(fieldInfo.canBeAnnotatedWithDeepPersonalData()).isFalse();
    }

    private FieldInfo fieldInfo(Class<?> clazz, String fieldName) {
        try {
            return new FieldInfo(clazz.getDeclaredField(fieldName));
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private static class SomeTestClass<T extends SomeOtherClass, S extends String, U extends S> {
        private UUID uuid;
        private String stringField;
        private int intField;
        private Integer integerField;
        private String[] arrayOfStrings;
        private Collection<String> collectionOfStrings;
        private Collection<? extends String> collectionOfStringExtensions;
        private Collection<? super String> collectionOfStringSupers;
        private Collection<T> collectionParameterizedOtherClasses;
        private Collection<S> collectionParameterizedStringExtensions;
        private int[] arrayOfInts;
        private Collection<Number> collectionOfNumbers;
        private Collection<? extends Number> collectionOfNumberExtensions;
        private BigDecimal bigDecimalField;
        private double primitiveDoubleField;
        private Double doubleField;
        private float primitiveFloatField;
        private Float floatField;
        private Collection<? super S> collectionOfParameterizedStringSupers;
        private Collection<? extends U> collectionOfParameterizedParameterizedStringExtensions;

    }

    private static class SomeOtherClass {

    }
}