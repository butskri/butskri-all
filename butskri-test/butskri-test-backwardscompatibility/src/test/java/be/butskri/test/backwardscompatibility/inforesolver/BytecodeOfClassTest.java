package be.butskri.test.backwardscompatibility.inforesolver;

import be.butskri.test.backwardscompatibility.subclasses.Bla;
import org.junit.Test;

import java.io.IOException;

import static be.butskri.test.backwardscompatibility.hashing.HashingAlgorithm.MD5;
import static org.assertj.core.api.Assertions.assertThat;

public class BytecodeOfClassTest {

    private BytecodeOfClass resolver = new BytecodeOfClass();

    @Test
    public void getName() {
        assertThat(resolver.getName()).isEqualTo("bytecode");
    }

    @Test
    public void hash() throws IOException {
        assertThat(resolver.getHashedData(Bla.class, MD5))
                .isEqualTo("cf48b1e3b24840ef50e7e6cc9ce12a0a");
    }
}