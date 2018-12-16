package be.butskri.test.backwardscompatibility.inforesolver;

import be.butskri.test.backwardscompatibility.HashBuilder;
import be.butskri.test.backwardscompatibility.hashing.HashingAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class BytecodeResolver implements ClassInfoResolver {

    private static Logger LOGGER = LoggerFactory.getLogger(HashBuilder.class);

    @Override
    public String resolveHashedInfo(Class<?> clazz, HashingAlgorithm hashingAlgorithm) throws IOException {
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
