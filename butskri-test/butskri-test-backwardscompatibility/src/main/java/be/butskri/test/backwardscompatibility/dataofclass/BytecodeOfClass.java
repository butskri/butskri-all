package be.butskri.test.backwardscompatibility.dataofclass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

public class BytecodeOfClass implements DataOfClass {

    private static Logger LOGGER = LoggerFactory.getLogger(BytecodeOfClass.class);

    public static BytecodeOfClass bytecode() {
        return new BytecodeOfClass();
    }

    private BytecodeOfClass() {
    }

    @Override
    public InputStream getDataAsStream(Class<?> clazz) {
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
