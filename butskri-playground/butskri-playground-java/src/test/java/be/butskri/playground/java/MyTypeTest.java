package be.butskri.playground.java;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

// acronyms used:
// gtofda = genericTypeOfFieldDeclaredAs
public class MyTypeTest {

    @Test
    public void gtofdaPrimitive_is_primitiveClass() throws NoSuchFieldException {
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
    public void gtofdaSimpleType_is_theSimpleClassItself() throws NoSuchFieldException {
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
    public void gtofdaArray_is_arrayClassWithComponentType() throws NoSuchFieldException {
        Field field = InnerClass.class.getDeclaredField("myArray");

        Type type = field.getGenericType();

        assertThat(type).isInstanceOf(Class.class);
        Class clazz = (Class) type;
        assertThat(clazz.isPrimitive()).isFalse();
        assertThat(clazz.isArray()).isTrue();
        assertThat(clazz.getComponentType()).isEqualTo(MySimpleType.class);
    }

    @Test
    public void gtofdaArrayWithTypeDeclaredInClass_is_genericArrayTypeWithGenericComponentType_ofType_TypeVariable() throws NoSuchFieldException {
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
    public void gtofdaCollectionWithGenericTypeDeclaredInClass_is_parameterizedTypeWithTypeArguments_ofType_TypeVariable() throws NoSuchFieldException {
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
    public void gtofdaCollectionWithDeclaredGenericType_is_parameterizedTypeWithTypeArgumentsTheDeclaredGenericType() throws NoSuchFieldException {
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
    public void gtofdaCollectionWithGenericTypeDeclaredWithExtends_is_parameterizedTypeWithTypeArguments_ofType_WildcardType() throws NoSuchFieldException {
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
    public void gtofdaCollectionWithGenericTypeDeclaredWithSuper_is_parameterizedTypeWithTypeArguments_ofType_WildcardType() throws NoSuchFieldException {
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

    private static class InnerClass<T extends MySimpleType> {
        private int myPrimitive;
        private MySimpleType mySimpleType;
        private MySimpleType[] myArray;
        private T[] myGenerifiedArray;
        private Collection<T> myGenerifiedCollection;
        private Collection<MySimpleType> myCollection;
        private Collection<? extends MySimpleType> myExtendedCollection;
        private Collection<? super MySimpleType> mySuperCollection;
    }

    private static class MySimpleType {

    }
}
