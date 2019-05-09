package be.kindengezin.groeipakket.backwardscompatibility.json.reflection;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ClassFinder {

    private Reflections reflections;

    public ClassFinder(String basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public <T> Collection<Class<?>> findAllNonAbstractSubclassesOf(Class<T> baseClass) {
        return reflections.getSubTypesOf(baseClass)
                .stream()
                .filter(nonAbstractClasses())
                .collect(Collectors.toSet());
    }

    public <T extends Annotation> Collection<Class<?>> findAllNonAbstractClassesAnnotatedWith(Class<T> baseClass) {
        return reflections.getTypesAnnotatedWith(baseClass)
                .stream()
                .filter(nonAbstractClasses())
                .collect(Collectors.toSet());
    }

    private static <T> Predicate<Class<? extends T>> nonAbstractClasses() {
        return clazz -> !Modifier.isAbstract(clazz.getModifiers());
    }
}
