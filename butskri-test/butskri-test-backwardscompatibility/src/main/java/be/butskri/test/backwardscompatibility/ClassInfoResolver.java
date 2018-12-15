package be.butskri.test.backwardscompatibility;

import java.io.IOException;

public interface ClassInfoResolver {

    BytecodeResolver BYTECODE_RESOLVER = new BytecodeResolver();

    default String getName() {
        return getClass().getSimpleName()
                .replaceAll("(Class)?(Info)?Resolver", "")
                .toLowerCase();
    }

    String resolveHashedInfo(Class<?> clazz, HashingAlgorithm hashingAlgorithm) throws IOException;

}
