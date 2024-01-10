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
        assertThat(md5Hash().hash(resolver.getDataAsStream(Bla.class)))
                .isEqualTo("4dc88a33b193d77d4a553f8ca42445dc");
    }
}