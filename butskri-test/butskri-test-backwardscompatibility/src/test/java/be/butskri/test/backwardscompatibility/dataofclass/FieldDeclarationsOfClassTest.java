package be.butskri.test.backwardscompatibility.dataofclass;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static be.butskri.test.backwardscompatibility.dataofclass.FieldDeclarationsOfClass.fieldDeclarations;
import static org.assertj.core.api.Assertions.assertThat;

public class FieldDeclarationsOfClassTest {

    private FieldDeclarationsOfClass fieldDeclarations = fieldDeclarations();

    @Test
    public void getDataAsStream_returnsInputStreamContainingFieldData() throws IOException {
        InputStream stream = fieldDeclarations.getDataAsStream(ClassWithThreeFields.class);

        List<String> lines = IOUtils.readLines(stream, "UTF-8");
        assertThat(lines).containsExactly(
                "private int be.butskri.test.backwardscompatibility.dataofclass.ClassWithThreeFields.def",
                "private java.lang.String be.butskri.test.backwardscompatibility.dataofclass.ClassWithThreeFields.abc",
                "private java.lang.String be.butskri.test.backwardscompatibility.dataofclass.ClassWithThreeFields.ghi"
        );
    }

    @Test
    public void getDataAsStream_fieldsOfSuperClassAreNotReturned() throws IOException {
        InputStream stream = fieldDeclarations.getDataAsStream(SubClass.class);

        List<String> lines = IOUtils.readLines(stream, "UTF-8");

        assertThat(lines).containsExactly(
                "private java.lang.String be.butskri.test.backwardscompatibility.dataofclass.SubClass.shownField"
        );
    }
}