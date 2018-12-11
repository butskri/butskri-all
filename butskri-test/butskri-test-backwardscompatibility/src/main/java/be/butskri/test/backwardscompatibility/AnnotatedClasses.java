package be.butskri.test.backwardscompatibility;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class AnnotatedClasses extends Classes<AnnotatedClasses> implements AnnotatedClassesBuilder {

    private final Class<? extends Annotation> annotation;
    private String packageName;

    public static AnnotatedClassesBuilder classesAnnotatedWith(Class<? extends Annotation> annotation) {
        return new AnnotatedClasses(annotation);
    }

    private AnnotatedClasses(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
        namedAs("classes-annotated-with-" + annotation.getSimpleName());
    }

    @Override
    Collection<Class> getAllClasses() {
        Reflections reflections = new Reflections(packageName);
        Set<Class> result = new HashSet<>(reflections.getTypesAnnotatedWith(annotation));
        return result;
    }

    @Override
    public AnnotatedClasses inPackage(String packageName) {
        this.packageName = packageName;
        return this;
    }

}
