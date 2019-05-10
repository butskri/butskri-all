package be.kindengezin.groeipakket.backwardscompatibility.json.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Collection;

public class FieldInfo {

    private Field field;

    public FieldInfo(Field field) {
        this.field = field;
    }

    public String getFieldName() {
        return field.getName();
    }

    public <T extends Annotation> boolean isAnnotatedWith(Class<T> annotation) {
        return field.getAnnotation(annotation) != null;
    }

    public boolean isArray() {
        return field.getType().isArray();
    }

    public boolean isCollection() {
        return isCollection(field.getType());
    }

    public Class<?> getType() {
        return field.getType();
    }

    public Class<?> underlyingType() {
        if (isArray()) {
            return field.getType().getComponentType();
        } else if (isCollection()) {
            return underlyingType(field.getGenericType());
        }
        return this.field.getType();
    }

    public Class<?> underlyingType(Type type) {
        if (type instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType) type;
            return underlyingType((wildcardType).getUpperBounds()[0]);
        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            return underlyingType(actualTypeArguments[0]);
        } else if (type instanceof TypeVariable) {
            TypeVariable typeVariable = (TypeVariable) type;
            Type[] bounds = typeVariable.getBounds();
            return underlyingType(bounds[0]);
        }
        return (Class<?>) type;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", field.getName(), typeAsString());
    }

    private String typeAsString() {
        if (isArray()) {
            return String.format("%s[]", underlyingType().getSimpleName());
        } else if (isCollection()) {
            return String.format("%s<%s>", field.getType().getSimpleName(), underlyingType().getSimpleName());
        }
        return underlyingType().getSimpleName();
    }

    private boolean isCollection(Class<?> type) {
        return Collection.class.isAssignableFrom(type);
    }
}
