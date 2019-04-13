package be.butskri.playground.keng.commons.test.json;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class DeepAssertions {

    public static void assertNoDeepNullValues(Object actualObject) {
        assertThat(actualObject).isNotNull();
        List<PropertyPath> nullFields = findDeepNullFields(actualObject);

        assertThat(nullFields)
                .describedAs(String.format("object of type %s should not have null values", actualObject.getClass()))
                .isEmpty();
    }

    private static List<PropertyPath> findDeepNullFields(Object object) {
        return findDeepNullFields(object, new PropertyPath());
    }

    private static List<PropertyPath> findDeepNullFields(Object object, PropertyPath path) {
        List<PropertyPath> result = new ArrayList<>();
        if (object == null) {
            result.add(path);
        } else {
            if (isCollectionLike(object)) {
                List<Object> list = toList(object);
                result.addAll(deepNullFieldsForList(list, path));
            } else if (shouldBeCheckedForNestedNullValues(object)) {
                result.addAll(deepNullFieldsForObject(object, path));
            }
        }
        return result;
    }

    private static List<PropertyPath> deepNullFieldsForList(List<Object> list, PropertyPath path) {
        List<PropertyPath> deepNullFields = new ArrayList<>();
        if (list.isEmpty()) {
            deepNullFields.add(path);
        } else {
            for (int i = 0; i < list.size(); i++) {
                deepNullFields.addAll(findDeepNullFields(list.get(i), path.hop("[" + i + "]")));
            }
        }
        return deepNullFields;
    }

    private static List deepNullFieldsForObject(Object object, PropertyPath path) {
        List<Field> fields = getAllNonTransientInstanceFields(object.getClass());
        return fields.stream()
                .map(field -> findDeepNullFields(getValue(object, field), path.hop(field)))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private static Object getValue(Object object, Field field) {
        field.setAccessible(true);
        try {
            return field.get(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(String.format("problem accessing field %s from object %s", field.getName(), object), e);
        }
    }

    private static List<Object> toList(Object object) {
        if (isArray(object)) {
            Object[] array = (Object[]) object;
            return Arrays.asList(array);
        } else {
            Collection<?> collection = (Collection<?>) object;
            return new ArrayList<>(collection);
        }
    }

    private static boolean isArray(Object object) {
        return object.getClass().isArray();
    }

    private static List<Field> getAllNonTransientInstanceFields(Class<?> clazz) {
        if (Object.class.equals(clazz)) {
            return Collections.emptyList();
        }
        List<Field> result = new ArrayList<>();
        result.addAll(Arrays.asList(clazz.getDeclaredFields()));
        result.addAll(getAllNonTransientInstanceFields(clazz.getSuperclass()));
        return result.stream()
                .filter(DeepAssertions::isNonStatic)
                .filter(DeepAssertions::isNonTransient)
                .collect(Collectors.toList());
    }

    private static boolean isNonStatic(Field field) {
        return !Modifier.isStatic(field.getModifiers());
    }

    private static boolean isNonTransient(Field field) {
        return !Modifier.isTransient(field.getModifiers());
    }

    private static boolean isCollectionLike(Object object) {
        return isArray(object) || Collection.class.isInstance(object);
    }

    private static boolean shouldBeCheckedForNestedNullValues(Object object) {
        if (object.getClass().isPrimitive()) {
            return false;
        } else if (object.getClass().getName().startsWith("java.lang")) {
            return false;
        }
        return true;
    }

    private static class PropertyPath {
        private final List<String> allFields;

        private PropertyPath() {
            this.allFields = new ArrayList<>();
        }

        private PropertyPath(List<String> fields) {
            this.allFields = fields;
        }

        public PropertyPath hop(String field) {
            List<String> fields = new ArrayList<>(allFields);
            fields.add(field);
            return new PropertyPath(fields);
        }

        public PropertyPath hop(Field field) {
            return hop("." + field.getName());
        }

        @Override
        public String toString() {
            String result = allFields.stream().collect(Collectors.joining(""));
            if (result.startsWith(".")) {
                result = result.substring(1);
            }
            return result;
        }
    }
}
