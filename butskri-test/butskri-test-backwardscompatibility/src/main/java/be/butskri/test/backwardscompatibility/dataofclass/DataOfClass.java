package be.butskri.test.backwardscompatibility.dataofclass;

import java.io.IOException;
import java.io.InputStream;

public interface DataOfClass {

    default String getName() {
        return getClass().getSimpleName()
                .replaceAll("(Of)?Class", "")
                .toLowerCase();
    }

    InputStream getDataAsStream(Class<?> clazz) throws IOException;

}
