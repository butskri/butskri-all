package be.butskri.test.backwardscompatibility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;

import java.io.InputStream;

public class HashBuilder {
    private static Logger LOGGER = LoggerFactory.getLogger(HashBuilder.class);
    private Class<?> clazz;

    HashBuilder(Class<?> clazz) {
        this.clazz = clazz;
    }

    String buildHash() {
        try {
            InputStream stream = resourceAsStream();
            return DigestUtils.md5DigestAsHex(stream);
        } catch (Exception e) {
            LOGGER.error("Problem loading resource {} for class {}", classResourceName(), clazz, e);
            throw new RuntimeException(e);
        }
    }

    private InputStream resourceAsStream() {
        LOGGER.info("getting resource {}", classResourceName());
        return clazz.getResourceAsStream(classResourceName());
    }

    private String classResourceName() {
        return getClassName(this.clazz) + ".class";
    }

    private String getClassName(Class<?> theClazz) {
        if (theClazz.getEnclosingClass() == null) {
            return theClazz.getSimpleName();
        }
        return getClassName(theClazz.getEnclosingClass()) + "$" + theClazz.getSimpleName();
    }
}
