package be.butskri.test.backwardscompatibility.hashing;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public interface HashingAlgorithm {

    static ByteArrayInputStream toInputStream(String stringToBeHashed) throws UnsupportedEncodingException {
        return new ByteArrayInputStream(stringToBeHashed.getBytes("UTF-8"));
    }

    default String getName() {
        return getClass().getSimpleName()
                .replaceAll("(Hashing)?Algorithm", "-hash")
                .toLowerCase();
    }

    String hash(InputStream stream) throws IOException;
}
