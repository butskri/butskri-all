package be.butskri.test.backwardscompatibility.dataofclass;

import be.butskri.test.backwardscompatibility.hashing.HashingAlgorithm;

import java.io.IOException;

public interface DataOfClass {

    default String getName() {
        return getClass().getSimpleName()
                .replaceAll("(Of)?Class", "")
                .toLowerCase();
    }

    String getHashedData(Class<?> clazz, HashingAlgorithm hashingAlgorithm) throws IOException;

}
