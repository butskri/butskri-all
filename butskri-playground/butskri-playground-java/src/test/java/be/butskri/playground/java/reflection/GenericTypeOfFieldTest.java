package be.butskri.playground.java.reflection;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public class GenericTypeOfFieldTest {

    @Test
    public void typeOfPrimitive_is_primitiveClass() throws NoSuchFieldException {
        Field field = InnerClass.class.getDeclaredField("myPrimitive");

        Type type = field.getGenericType();

        assertThat(type).isInstanceOf(Class.class);
        Class clazz = (Class) type;
        assertThat(clazz.isPrimitive()).isTrue();
        assertThat(clazz.isArray()).isFalse();
        assertThat(clazz.getComponentType()).isNull();
        assertThat(clazz).isEqualTo(int.class);
    }

    @Test
    public void typeOfSimpleType_is_theSimpleClassItself() throws NoSuchFieldException {
        Field field = InnerClass.class.getDeclaredField("mySimpleType");

        Type type = field.getGenericType();

        assertThat(type).isInstanceOf(Class.class);
        Class clazz = (Class) type;
        assertThat(clazz.isPrimitive()).isFalse();
        assertThat(clazz.isArray()).isFalse();
        assertThat(clazz.getComponentType()).isNull();
        assertThat(clazz).isEqualTo(MySimpleType.class);
    }

    @Test
    public void typeOfArray_is_arrayClassWithComponentType() throws NoSuchFieldException {
        Field field = InnerClass.class.getDeclaredField("myArray");

        Type type = field.getGenericType();

        assertThat(type).isInstanceOf(Class.class);
        Class clazz = (Class) type;
        assertThat(clazz.isPrimitive()).isFalse();
        assertThat(clazz.isArray()).isTrue();
        assertThat(clazz.getComponentType()).isEqualTo(MySimpleType.class);
    }

    @Test
    public void typeOfArrayWithTypeDeclaredInClass_is_genericArrayTypeWithGenericComponentTypeVariable() throws NoSuchFieldException {
        Field field = InnerClass.class.getDeclaredField("myGenerifiedArray");

        Type type = field.getGenericType();

        assertThat(type).isInstanceOf(GenericArrayType.class);
        GenericArrayType genericArrayType = (GenericArrayType) type;
        assertThat(genericArrayType.getGenericComponentType()).isInstanceOf(TypeVariable.class);
        TypeVariable typeVariable = (TypeVariable) genericArrayType.getGenericComponentType();
        Type[] bounds = typeVariable.getBounds();
        assertThat(bounds).hasSize(1);
        assertThat(bounds[0]).isEqualTo(MySimpleType.class);
    }

    @Test
    public void typeOfCollectionWithGenericTypeDeclaredInClass_is_parameterizedTypeWithTypeArgumentsTypeVariable() throws NoSuchFieldException {
        Field field = InnerClass.class.getDeclaredField("myGenerifiedCollection");

        Type type = field.getGenericType();

        assertThat(type).isInstanceOf(ParameterizedType.class);
        ParameterizedType parameterizedType = (ParameterizedType) type;
        assertThat(parameterizedType.getRawType()).isEqualTo(Collection.class);
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        assertThat(actualTypeArguments).hasSize(1);
        assertThat(actualTypeArguments[0]).isInstanceOf(TypeVariable.class);
        TypeVariable typeVariable = (TypeVariable) actualTypeArguments[0];
        Type[] bounds = typeVariable.getBounds();
        assertThat(bounds).hasSize(1);
        assertThat(bounds[0]).isEqualTo(MySimpleType.class);
    }

    @Test
    public void typeOfCollectionWithDeclaredGenericType_is_parameterizedTypeWithTypeArgumentsTheDeclaredGenericType() throws NoSuchFieldException {
        Field field = InnerClass.class.getDeclaredField("myCollection");
        Type type = field.getGenericType();

        assertThat(type).isInstanceOf(ParameterizedType.class);
        ParameterizedType parameterizedType = (ParameterizedType) type;
        assertThat(parameterizedType.getRawType()).isEqualTo(Collection.class);
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        assertThat(actualTypeArguments).hasSize(1);
        assertThat(actualTypeArguments[0]).isEqualTo(MySimpleType.class);
    }

    @Test
    public void typeOfCollectionWithGenericTypeDeclaredWithExtends_is_parameterizedTypeWithTypeArgumentsWildcardType() throws NoSuchFieldException {
        Field field = InnerClass.class.getDeclaredField("myExtendedCollection");
        Type type = field.getGenericType();

        assertThat(type).isInstanceOf(ParameterizedType.class);
        ParameterizedType parameterizedType = (ParameterizedType) type;
        assertThat(parameterizedType.getRawType()).isEqualTo(Collection.class);
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        assertThat(actualTypeArguments).hasSize(1);
        assertThat(actualTypeArguments[0]).isInstanceOf(WildcardType.class);
        WildcardType wildcardType = (WildcardType) actualTypeArguments[0];
        assertThat(wildcardType.getLowerBounds()).hasSize(0);
        assertThat(wildcardType.getUpperBounds()).hasSize(1);
        assertThat(wildcardType.getUpperBounds()[0]).isEqualTo(MySimpleType.class);
    }

    @Test
    public void typeOfCollectionWithGenericTypeDeclaredWithSuper_is_parameterizedTypeWithTypeArgumentsWildcardType() throws NoSuchFieldException {
        Field field = InnerClass.class.getDeclaredField("mySuperCollection");
        Type type = field.getGenericType();

        assertThat(type).isInstanceOf(ParameterizedType.class);
        ParameterizedType parameterizedType = (ParameterizedType) type;
        assertThat(parameterizedType.getRawType()).isEqualTo(Collection.class);
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        assertThat(actualTypeArguments).hasSize(1);
        assertThat(actualTypeArguments[0]).isInstanceOf(WildcardType.class);
        WildcardType wildcardType = (WildcardType) actualTypeArguments[0];
        assertThat(wildcardType.getLowerBounds()).hasSize(1);
        assertThat(wildcardType.getLowerBounds()[0]).isEqualTo(MySimpleType.class);
        assertThat(wildcardType.getUpperBounds()).hasSize(1);
        assertThat(wildcardType.getUpperBounds()[0]).isEqualTo(Object.class);
    }

    @Test
    public void typeOfCustomTypeWithRecurringGenerics_is_parameterizedTypeWithRecurringTypeVariable() throws NoSuchFieldException {
        Field field = InnerClass.class.getDeclaredField("myGenericType");
        Type type = field.getGenericType();

        assertThat(type).isInstanceOf(ParameterizedType.class);
        ParameterizedType parameterizedType = (ParameterizedType) type;
        assertThat(parameterizedType.getRawType()).isEqualTo(MyGenericType.class);
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        assertThat(actualTypeArguments).hasSize(1);
        assertThat(actualTypeArguments[0]).isInstanceOf(TypeVariable.class);

        TypeVariable typeVariable = (TypeVariable) actualTypeArguments[0];
        assertThat(typeVariable.getBounds()).hasSize(1);
        assertThat(typeVariable.getBounds()[0]).isInstanceOf(ParameterizedType.class);
        ParameterizedType parameterizedType2 = (ParameterizedType) typeVariable.getBounds()[0];
        assertThat(parameterizedType2.getRawType()).isEqualTo(Comparable.class);
        assertThat(parameterizedType2.getActualTypeArguments()).hasSize(1);
        assertThat(parameterizedType2.getActualTypeArguments()[0]).isEqualTo(typeVariable);
    }

    @Test
    public void typeOfCustomTypeWithGenericType_is_parameterizedTypeWithTypeArgumentClass() throws NoSuchFieldException {
        Field field = InnerClass.class.getDeclaredField("myGenericTypeWithInteger");
        Type type = field.getGenericType();

        assertThat(type).isInstanceOf(ParameterizedType.class);
        ParameterizedType parameterizedType = (ParameterizedType) type;
        assertThat(parameterizedType.getRawType()).isEqualTo(MyGenericType.class);
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        assertThat(actualTypeArguments).hasSize(1);
        assertThat(actualTypeArguments[0]).isEqualTo(Integer.class);
    }

    private static class InnerClass<T extends MySimpleType, C extends Comparable<C>> {
        private int myPrimitive;
        private MySimpleType mySimpleType;
        private MySimpleType[] myArray;
        private T[] myGenerifiedArray;
        private Collection<T> myGenerifiedCollection;
        private Collection<MySimpleType> myCollection;
        private Collection<? extends MySimpleType> myExtendedCollection;
        private Collection<? super MySimpleType> mySuperCollection;
        private MyGenericType<C> myGenericType;
        private MyGenericType<Integer> myGenericTypeWithInteger;
    }

    private static class MySimpleType {

    }

    private static class MyGenericType<T extends Comparable<T>> {
        private T someVariable;
    }
}
