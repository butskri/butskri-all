package be.butskri.test.backwardscompatibility.classes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.reflect.Modifier.isStatic;

public class FieldTypes extends Classes<FieldTypes> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FieldTypes.class);
    private Collection<Class> classes;

    public static FieldTypes of(Classes classes) {
        return new FieldTypes(classes);
    }

    private FieldTypes(Classes classes) {
        this.classes = classes.getClasses();
        this.namedAs("fieldtypes-of-" + classes.getName());
    }

    @Override
    Collection<Class> getAllClasses() {
        FieldTypesCollector collector = new FieldTypesCollector(excludingPredicate);
        classes.stream()
                .forEach(clazz -> collector.addFieldsOf(clazz));

        return collector.getClasses();
    }

    private static class FieldTypesCollector {
        private final Predicate<Class> excludingPredicate;
        private final Collection<Class> classes = new HashSet<>();
        private final Collection<Type> types = new HashSet<>();

        public FieldTypesCollector(Predicate<Class> excludingPredicate) {
            this.excludingPredicate = excludingPredicate;
        }

        public void addFieldsOf(Class clazz) {
            distinctFieldTypesFor(clazz)
                    .stream()
                    .forEach(this::addType);
        }

        private Set<Type> distinctFieldTypesFor(Class clazz) {
            Set<Type> result = new HashSet<>();
            result.addAll(fieldTypesFor(clazz));
            if (clazz.getSuperclass() != null) {
                result.addAll(distinctFieldTypesFor(clazz.getSuperclass()));
            }
            return result;
        }

        private Set<Type> fieldTypesFor(Class clazz) {
            // TODO check what to do with this
            if (String.class.equals(clazz)) {
                return Collections.emptySet();
            }
            return Stream.of(clazz.getDeclaredFields())
                    .filter(this::isNonStaticField)
                    .map(Field::getGenericType)
                    .collect(Collectors.toSet());
        }

        private void addFieldTypeAndNestedTypes(Class clazz) {
            if (excludingPredicate.test(clazz)) {
                return;
            }
            if (classes.contains(clazz)) {
                return;
            }
            if (clazz.isArray()) {
                addFieldTypeAndNestedTypes(clazz.getComponentType());
                return;
            }
            classes.add(clazz);
            distinctFieldTypesFor(clazz)
                    .stream()
                    .forEach(this::addType);
        }

        private void addType(Type type) {
            if (this.types.contains(type)) {
                return;
            }
            this.types.add(type);
            if (isClass(type)) {
                addFieldTypeAndNestedTypes((Class) type);
            } else if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                if (parameterizedType.getOwnerType() != null) {
                    addType(parameterizedType.getOwnerType());
                }
                if (parameterizedType.getRawType() != null) {
                    addType(parameterizedType.getRawType());
                }
                Stream.of(parameterizedType.getActualTypeArguments())
                        .forEach(this::addType);
            } else {
                LOGGER.warn("unrecognized generic type {}", type);
            }
        }

        private boolean isClass(Type type) {
            return Class.class.equals(type.getClass());
        }

        private boolean isNonStaticField(Field field) {
            return !isStatic(field.getModifiers());
        }

        private Collection<Class> getClasses() {
            return classes;
        }
    }

}
