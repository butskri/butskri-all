package be.butskri.test.backwardscompatibility.hashing;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class Md5HashingAlgorithmTest {

    private Md5HashingAlgorithm hashingAlgorithm = new Md5HashingAlgorithm();

    @Test
    public void getNameReturnsExpectedName() {
        assertThat(hashingAlgorithm.getName()).isEqualTo("md5-hash");
    }

    @Test
    public void hashAlwaysReturnsStringOf32Characters() throws IOException {
        String stringToBeHashed = randomStringWithRandomLength();

        assertThat(hashingAlgorithm.hash(stringToBeHashed)).hasSize(32);
    }

    @Test
    public void hashReturnsMd5Hash() throws IOException {
        String stringToBeHashed = "this is the string that needs to be hashed. " +
                "We can predict the result since it will always be the same for the same input.";

        assertThat(hashingAlgorithm.hash(stringToBeHashed)).isEqualTo("a57301d0bd7bea8636d56a71359b134e");
    }

    private String randomStringWithRandomLength() {
        int randomlengthOfAdditionalString = RandomUtils.nextInt(2000, 5000);
        return "someStringWithRandomValue" + UUID.randomUUID().toString() + RandomStringUtils.random(randomlengthOfAdditionalString);
    }

}