package be.butskri.test.backwardscompatibility.hashing;

import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.io.InputStream;

public class Md5HashingAlgorithm implements HashingAlgorithm {

    public static Md5HashingAlgorithm md5Hash() {
        return new Md5HashingAlgorithm();
    }

    private Md5HashingAlgorithm() {
    }

    @Override
    public String hash(InputStream stream) throws IOException {
        return DigestUtils.md5DigestAsHex(stream);
    }
}
