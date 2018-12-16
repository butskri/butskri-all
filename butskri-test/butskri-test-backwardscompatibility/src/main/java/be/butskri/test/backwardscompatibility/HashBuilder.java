package be.butskri.test.backwardscompatibility;

import be.butskri.test.backwardscompatibility.hashing.HashingAlgorithm;
import be.butskri.test.backwardscompatibility.inforesolver.ClassInfoResolver;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class HashBuilder {
    private static Logger LOGGER = LoggerFactory.getLogger(HashBuilder.class);

    private List<ClassInfoResolver> infoResolvers = Lists.newArrayList(ClassInfoResolver.BYTECODE_RESOLVER);
    private HashingAlgorithm hashingAlgorithm = HashingAlgorithm.MD5;

    HashBuilder() {
    }

    String hashFor(Class<?> clazz) {
        return infoResolvers.stream()
                .map(resolver -> resolveHash(clazz, resolver))
                .collect(Collectors.joining("_"));
    }

    private String resolveHash(Class<?> clazz, ClassInfoResolver resolver) {
        try {
            return resolver.resolveHashedInfo(clazz, hashingAlgorithm);
        } catch (IOException e) {
            String logId = UUID.randomUUID().toString().replaceAll("-", "");
            LOGGER.error("Problem calculating hash {}", logId, e);
            return "ERROR-" + logId;
        }
    }
}
