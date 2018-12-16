package be.butskri.test.backwardscompatibility.dataofclass;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

import static be.butskri.test.backwardscompatibility.hashing.HashingAlgorithm.toInputStream;

public class FieldDeclarationsOfClass implements DataOfClass {

    public static FieldDeclarationsOfClass fieldDeclarations() {
        return new FieldDeclarationsOfClass();
    }

    private FieldDeclarationsOfClass() {
    }

    @Override
    public InputStream getDataAsStream(Class<?> clazz) throws IOException {
        return toInputStream(getDataAsString(clazz));
    }

    private String getDataAsString(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .map(Field::toString)
                .sorted()
                .collect(Collectors.joining("\n"));
    }

}
