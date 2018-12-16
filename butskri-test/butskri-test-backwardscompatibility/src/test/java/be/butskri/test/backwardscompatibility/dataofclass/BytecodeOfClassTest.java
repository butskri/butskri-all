package be.butskri.test.backwardscompatibility.dataofclass;

import be.butskri.test.backwardscompatibility.subclasses.Bla;
import org.junit.Test;

import java.io.IOException;

import static be.butskri.test.backwardscompatibility.hashing.Md5HashingAlgorithm.md5Hash;
import static org.assertj.core.api.Assertions.assertThat;

public class BytecodeOfClassTest {

    private BytecodeOfClass resolver = BytecodeOfClass.bytecode();

    @Test
    public void getName() {
        assertThat(resolver.getName()).isEqualTo("bytecode");
    }

    @Test
    public void hash() throws IOException {
        assertThat(resolver.getHashedData(Bla.class, md5Hash()))
                .isEqualTo("cf48b1e3b24840ef50e7e6cc9ce12a0a");
    }
}