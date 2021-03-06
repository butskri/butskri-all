package be.butskri.test.backwardscompatibility.hashing;

import be.butskri.test.backwardscompatibility.dataofclass.DataOfClass;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class SimpleHashCalculator implements HashCalculator {
    private static Logger LOGGER = LoggerFactory.getLogger(SimpleHashCalculator.class);

    private List<DataOfClass> dataOfClass;
    private HashingAlgorithm hashingAlgorithm;

    SimpleHashCalculator(HashingAlgorithm hashingAlgorithm, DataOfClass... dataOfClass) {
        this.dataOfClass = Lists.newArrayList(dataOfClass);
        this.hashingAlgorithm = hashingAlgorithm;
    }

    @Override
    public String hashFor(Class<?> clazz) {
        return dataOfClass.stream()
                .map(resolver -> resolveHash(clazz, resolver))
                .collect(Collectors.joining("_"));
    }

    private String resolveHash(Class<?> clazz, DataOfClass resolver) {
        try (InputStream inputStream = resolver.getDataAsStream(clazz)) {
            return hashingAlgorithm.hash(inputStream);
        } catch (IOException e) {
            String logId = UUID.randomUUID().toString().replaceAll("-", "");
            LOGGER.error("Problem calculating hash {}", logId, e);
            return "ERROR-" + logId;
        }
    }
}
