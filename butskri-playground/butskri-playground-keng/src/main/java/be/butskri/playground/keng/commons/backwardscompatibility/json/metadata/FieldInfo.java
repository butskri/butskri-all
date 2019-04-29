package be.butskri.playground.keng.commons.backwardscompatibility.json.metadata;

import org.assertj.core.util.Sets;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public class FieldInfo {

    static final Set<Class<?>> PERSONAL_DATA_COMPLIANT_TYPES = Sets.newLinkedHashSet(
            String.class, LocalDate.class, LocalDateTime.class, UUID.class,
            Number.class, Integer.class, Long.class, BigDecimal.class, Double.class, Float.class
    );
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

    public boolean canBeAnnotatedWithPersonalData() {
        return isPersonalDataCompliantType();
    }

    public boolean canBeAnnotatedWithDeepPersonalData() {
        return !canBeAnnotatedWithPersonalData();
    }

    public boolean isArray() {
        return field.getType().isArray();
    }

    public boolean isCollection() {
        return isCollection(field.getType());
    }

    private boolean isPersonalDataCompliantType() {
        Class<?> underlyingType = underlyingType();
        return (underlyingType.isPrimitive() || PERSONAL_DATA_COMPLIANT_TYPES.contains(underlyingType));
    }

    public Class<?> underlyingType() {
        if (isArray()) {
            return field.getType().getComponentType();
        } else if (isCollection()) {
            ParameterizedType type = (ParameterizedType) field.getGenericType();
            Type[] actualTypeArguments = type.getActualTypeArguments();
            return underlyingType(actualTypeArguments[0]);
        }
        return this.field.getType();
    }

    public Class<?> underlyingType(Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        } else if (type instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType) type;
            return underlyingType(((WildcardType) type).getUpperBounds()[0]);
        }
        TypeVariable typeVariable = (TypeVariable) type;
        Type[] bounds = typeVariable.getBounds();
        return (Class<?>) bounds[0];
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
