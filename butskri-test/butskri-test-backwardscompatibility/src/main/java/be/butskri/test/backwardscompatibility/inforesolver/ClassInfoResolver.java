package be.butskri.test.backwardscompatibility.inforesolver;

import be.butskri.test.backwardscompatibility.hashing.HashingAlgorithm;

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
