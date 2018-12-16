package be.butskri.test.backwardscompatibility.hashing;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public interface HashingAlgorithm {

    HashingAlgorithm MD5 = new Md5HashingAlgorithm();

    default String getName() {
        return getClass().getSimpleName()
                .replaceAll("(Hashing)?Algorithm", "-hash")
                .toLowerCase();
    }

    String hash(InputStream stream) throws IOException;

    default String hash(String stringToBeHashed) throws IOException {
        try (InputStream stream = new ByteArrayInputStream(stringToBeHashed.getBytes("UTF-8"))) {
            return hash(stream);
        }
    }
}
