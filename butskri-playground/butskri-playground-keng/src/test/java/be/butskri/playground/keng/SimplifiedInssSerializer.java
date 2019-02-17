package be.butskri.playground.keng;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class SimplifiedInssSerializer extends JsonSerializer<SimplifiedInss> {

    @Override
    public void serialize(SimplifiedInss simplifiedInss, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(simplifiedInss.getValue());
    }
}
