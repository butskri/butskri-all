package be.butskri.test.backwardscompatibility;

import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class Classes<T extends Classes<T>> {

    private String name;
    Predicate<Class> excludingPredicate = (clazz) -> false;

    public static Subclasses subclassesOf(Class clazz) {
        return Subclasses.of(clazz);
    }

    public static Classes classes(Class... classes) {
        return Classes.classes(Sets.newHashSet(classes));
    }

    public static Classes classes(Collection<Class> classes) {
        return new Classes() {
            @Override
            public Collection<Class> getAllClasses() {
                return classes;
            }
        }.namedAs(String.valueOf(classes.size()) + "-classes");
    }

    public static Classes fieldTypesOf(Classes classes) {
        return FieldTypes.of(classes);
    }

    public T namedAs(String name) {
        this.name = name;
        return (T) this;
    }

    public T excludingPrimitives() {
        return excluding(clazz -> clazz.isPrimitive());
    }

    public T excludingStandardJavaClasses() {
        return excluding(clazz -> clazz.getName().startsWith("java."));
    }

    public T excluding(Collection<Class> classes) {
        return excluding(clazz -> classes.contains(clazz));
    }

    public T excluding(Predicate<Class> excludingPredicate) {
        this.excludingPredicate = this.excludingPredicate.or(excludingPredicate);
        return (T) this;
    }

    String getName() {
        return name;
    }

    Collection<Class> filteredClasses() {
        return getAllClasses()
                .stream()
                .filter(this.excludingPredicate.negate())
                .collect(Collectors.toSet());
    }

    abstract Collection<Class> getAllClasses();

}
