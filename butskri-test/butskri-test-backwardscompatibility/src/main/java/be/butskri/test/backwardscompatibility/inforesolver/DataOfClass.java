package be.butskri.test.backwardscompatibility.inforesolver;

import be.butskri.test.backwardscompatibility.hashing.HashingAlgorithm;

import java.io.IOException;

public interface DataOfClass {

    BytecodeOfClass BYTECODE_OF_CLASS = new BytecodeOfClass();

    default String getName() {
        return getClass().getSimpleName()
                .replaceAll("(Of)?Class", "")
                .toLowerCase();
    }

    String getHashedData(Class<?> clazz, HashingAlgorithm hashingAlgorithm) throws IOException;

}
