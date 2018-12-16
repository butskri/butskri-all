package be.butskri.test.backwardscompatibility;

import be.butskri.test.backwardscompatibility.hashing.HashingAlgorithm;
import be.butskri.test.backwardscompatibility.inforesolver.DataOfClass;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static be.butskri.test.backwardscompatibility.hashing.HashingAlgorithm.MD5;
import static be.butskri.test.backwardscompatibility.inforesolver.DataOfClass.BYTECODE_OF_CLASS;

public class HashCalculator {
    private static Logger LOGGER = LoggerFactory.getLogger(HashCalculator.class);

    public static HashCalculator defaultHashCalculator() {
        return new HashCalculator(MD5, BYTECODE_OF_CLASS);
    }

    private List<DataOfClass> dataOfClass;
    private HashingAlgorithm hashingAlgorithm;

    HashCalculator(HashingAlgorithm hashingAlgorithm, DataOfClass... dataOfClass) {
        this.dataOfClass = Lists.newArrayList(dataOfClass);
        this.hashingAlgorithm = hashingAlgorithm;
    }

    public String hashFor(Class<?> clazz) {
        return dataOfClass.stream()
                .map(resolver -> resolveHash(clazz, resolver))
                .collect(Collectors.joining("_"));
    }

    private String resolveHash(Class<?> clazz, DataOfClass resolver) {
        try {
            return resolver.getHashedData(clazz, hashingAlgorithm);
        } catch (IOException e) {
            String logId = UUID.randomUUID().toString().replaceAll("-", "");
            LOGGER.error("Problem calculating hash {}", logId, e);
            return "ERROR-" + logId;
        }
    }
}
