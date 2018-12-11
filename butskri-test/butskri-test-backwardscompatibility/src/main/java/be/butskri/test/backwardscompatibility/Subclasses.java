package be.butskri.test.backwardscompatibility;

import org.reflections.Reflections;

import java.util.Collection;
import java.util.Set;

public class Subclasses extends Classes {

    private String basePackage;
    private Class superType;

    public static Subclasses of(Class superType) {
        return new Subclasses(superType);
    }

    private Subclasses(Class superType) {
        namedAs("subclasses-of-" + superType.getSimpleName());
        this.superType = superType;
        inPackage(basePackageFor(superType));
    }

    public Subclasses inPackage(String basePackage) {
        this.basePackage = basePackage;
        return this;
    }

    @Override
    Collection<Class> getAllClasses() {
        Reflections reflections = new Reflections(basePackage);
        Set<Class> found = reflections.getSubTypesOf(superType);
        return found;
    }

    private static String basePackageFor(Class clazz) {
        String[] split = clazz.getName().split("\\.");
        if (split.length > 2) {
            return split[0] + "." + split[1];
        } else if (split.length > 1) {
            return split[0];
        }
        return "";
    }
}
