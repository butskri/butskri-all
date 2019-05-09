package be.kindengezin.groeipakket.backwardscompatibility.json.metadata;

import io.axoniq.gdpr.api.DeepPersonalData;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.reflect.Modifier.isStatic;
import static java.util.Collections.emptyList;

public class ClassInfo {

    private Class<?> clazz;

    public ClassInfo(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public static ClassInfo classInfoFor(Class<?> clazz) {
        return new ClassInfo(clazz);
    }

    public <T extends Annotation> Optional<FieldInfo> findSingleFieldInfoByAnnotation(Class<T> annotation) {
        return findFieldInfoByAnnotation(annotation).findFirst();
    }

    public <T extends Annotation> Collection<FieldInfo> findFieldInfoCollectionByAnnotation(Class<T> annotation) {
        return findFieldInfoByAnnotation(annotation).collect(Collectors.toList());
    }

    public <T extends Annotation> Stream<FieldInfo> findFieldInfoByAnnotation(Class<T> annotation) {
        return getAllInstanceFieldInfos()
                .stream()
                .filter(fieldInfo -> fieldInfo.isAnnotatedWith(annotation));
    }

    private List<FieldInfo> getAllInstanceFieldInfos() {
        return getAllInstanceFieldInfos(clazz);
    }

    private List<FieldInfo> getAllInstanceFieldInfos(Class<?> theClazz) {
        if (Object.class.equals(theClazz)) {
            return emptyList();
        } else {
            List<FieldInfo> result = Arrays.stream(theClazz.getDeclaredFields())
                    .filter(field -> !isStatic(field.getModifiers()))
                    .map(FieldInfo::new)
                    .collect(Collectors.toList());
            result.addAll(getAllInstanceFieldInfos(theClazz.getSuperclass()));
            return result;
        }
    }

    public Collection<Class<?>> getNestedDeepPersonalDataClasses() {
        Collection<Class<?>> result = new HashSet<>();
        findFieldInfoCollectionByAnnotation(DeepPersonalData.class)
                .stream()
                .map(FieldInfo::underlyingType)
                .forEach(clazz -> {
                    result.add(clazz);
                    result.addAll(new ClassInfo(clazz).getNestedDeepPersonalDataClasses());
                });
        return result;
    }
}
