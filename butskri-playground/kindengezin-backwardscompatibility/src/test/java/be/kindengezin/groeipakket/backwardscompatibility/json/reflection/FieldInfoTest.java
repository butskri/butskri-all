package be.kindengezin.groeipakket.backwardscompatibility.json.reflection;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class FieldInfoTest {

    @Test
    public void uuidFieldHasUnderlyingTypeUuid() {
        FieldInfo fieldInfo = fieldInfo(SomeTestClass.class, "uuid");

        assertThat(fieldInfo.underlyingType()).isEqualTo(UUID.class);
    }

    @Test
    public void stringFieldHasUnderlyingTypeString() {
        FieldInfo fieldInfo = fieldInfo(SomeTestClass.class, "stringField");

        assertThat(fieldInfo.underlyingType()).isEqualTo(String.class);
    }

    @Test
    public void intFieldHasUnderlyingTypeInt() {
        FieldInfo fieldInfo = fieldInfo(SomeTestClass.class, "intField");

        assertThat(fieldInfo.underlyingType()).isEqualTo(int.class);
    }

    @Test
    public void integerFieldHasUnderlyingTypeInteger() {
        FieldInfo fieldInfo = fieldInfo(SomeTestClass.class, "integerField");

        assertThat(fieldInfo.underlyingType()).isEqualTo(Integer.class);
    }

    @Test
    public void arrayOfStringsFieldHasUnderlyingTypeString() {
        FieldInfo fieldInfo = fieldInfo(SomeTestClass.class, "arrayOfStrings");

        assertThat(fieldInfo.underlyingType()).isEqualTo(String.class);
    }

    @Test
    public void collectionOfStringsFieldHasUnderlyingTypeString() {
        FieldInfo fieldInfo = fieldInfo(SomeTestClass.class, "collectionOfStrings");

        assertThat(fieldInfo.underlyingType()).isEqualTo(String.class);
    }

    @Test
    public void collectionOfStringExtensionsFieldHasUnderlyingTypeString() {
        FieldInfo fieldInfo = fieldInfo(SomeTestClass.class, "collectionOfStringExtensions");

        assertThat(fieldInfo.underlyingType()).isEqualTo(String.class);
    }

    @Test
    public void collectionOfStringSupersFieldHasUnderlyingTypeObject() {
        FieldInfo fieldInfo = fieldInfo(SomeTestClass.class, "collectionOfStringSupers");

        assertThat(fieldInfo.underlyingType()).isEqualTo(Object.class);
    }

    @Test
    public void collectionOfParameterizedExtensionsOfOtherClassesHasUnderlyingTypeSomeOtherClass() {
        FieldInfo fieldInfo = fieldInfo(SomeTestClass.class, "collectionParameterizedOtherClasses");

        assertThat(fieldInfo.underlyingType()).isEqualTo(SomeOtherClass.class);
    }

    @Test
    public void collectionOfParameterizedExtensionsOfStringsHasUnderlyingTypeString() {
        FieldInfo fieldInfo = fieldInfo(SomeTestClass.class, "collectionParameterizedStringExtensions");

        assertThat(fieldInfo.underlyingType()).isEqualTo(String.class);
    }

    @Test
    public void arrayOfIntsHasUnderlyingTypeInt() {
        FieldInfo fieldInfo = fieldInfo(SomeTestClass.class, "arrayOfInts");

        assertThat(fieldInfo.underlyingType()).isEqualTo(int.class);
    }

    @Test
    public void collectionOfNumbersHasUnderlyingTypeNumber() {
        FieldInfo fieldInfo = fieldInfo(SomeTestClass.class, "collectionOfNumbers");

        assertThat(fieldInfo.underlyingType()).isEqualTo(Number.class);
    }

    @Test
    public void collectionOfNumberExtensionsHasUnderlyingTypeNumber() {
        FieldInfo fieldInfo = fieldInfo(SomeTestClass.class, "collectionOfNumberExtensions");

        assertThat(fieldInfo.underlyingType()).isEqualTo(Number.class);
    }

    @Test
    public void bigDecimalFieldHasUnderlyingTypeBigDecimal() {
        FieldInfo fieldInfo = fieldInfo(SomeTestClass.class, "bigDecimalField");

        assertThat(fieldInfo.underlyingType()).isEqualTo(BigDecimal.class);
    }

    @Test
    public void floatFieldHasUnderlyingTypeFloat() {
        FieldInfo fieldInfo = fieldInfo(SomeTestClass.class, "floatField");

        assertThat(fieldInfo.underlyingType()).isEqualTo(Float.class);
    }

    @Test
    public void primitiveFloatFieldHasUnderlyingTypeFloat() {
        FieldInfo fieldInfo = fieldInfo(SomeTestClass.class, "primitiveFloatField");

        assertThat(fieldInfo.underlyingType()).isEqualTo(float.class);
    }

    @Test
    public void doubleFieldHasUnderlyingTypeDouble() {
        FieldInfo fieldInfo = fieldInfo(SomeTestClass.class, "doubleField");

        assertThat(fieldInfo.underlyingType()).isEqualTo(Double.class);
    }

    @Test
    public void primitiveDoubleFieldHasUnderlyingTypeDouble() {
        FieldInfo fieldInfo = fieldInfo(SomeTestClass.class, "primitiveDoubleField");

        assertThat(fieldInfo.underlyingType()).isEqualTo(double.class);
    }

    @Test
    public void collectionOfParameterizedStringSupersHasUnderlyingTypeObject() {
        FieldInfo fieldInfo = fieldInfo(SomeTestClass.class, "collectionOfParameterizedStringSupers");

        assertThat(fieldInfo.underlyingType()).isEqualTo(Object.class);
    }

    @Test
    public void collectionOfParameterizedParameterizedStringExtensionsHasUnderlyingTypeString() {
        FieldInfo fieldInfo = fieldInfo(SomeTestClass.class, "collectionOfParameterizedParameterizedStringExtensions");

        assertThat(fieldInfo.underlyingType()).isEqualTo(String.class);
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