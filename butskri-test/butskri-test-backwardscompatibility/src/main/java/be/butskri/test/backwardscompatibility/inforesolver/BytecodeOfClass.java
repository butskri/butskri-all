package be.butskri.test.backwardscompatibility.inforesolver;

import be.butskri.test.backwardscompatibility.hashing.HashCalculator;
import be.butskri.test.backwardscompatibility.hashing.HashingAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class BytecodeOfClass implements DataOfClass {

    private static Logger LOGGER = LoggerFactory.getLogger(HashCalculator.class);

    public static BytecodeOfClass bytecode() {
        return new BytecodeOfClass();
    }

    private BytecodeOfClass() {
    }

    @Override
    public String getHashedData(Class<?> clazz, HashingAlgorithm hashingAlgorithm) throws IOException {
        return hashingAlgorithm.hash(resourceAsStream(clazz));
    }

    private InputStream resourceAsStream(Class<?> clazz) {
        String resourceName = classResourceName(clazz);
        LOGGER.debug("getting resource {}", resourceName);
        return clazz.getResourceAsStream(resourceName);
    }

    private String classResourceName(Class<?> clazz) {
        return getClassName(clazz) + ".class";
    }

    private String getClassName(Class<?> theClazz) {
        if (theClazz.getEnclosingClass() == null) {
            return theClazz.getSimpleName();
        }
        return getClassName(theClazz.getEnclosingClass()) + "$" + theClazz.getSimpleName();
    }
}
