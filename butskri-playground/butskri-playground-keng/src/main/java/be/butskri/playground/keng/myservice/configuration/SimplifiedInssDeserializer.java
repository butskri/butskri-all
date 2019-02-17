package be.butskri.playground.keng.myservice.configuration;

import be.butskri.playground.keng.myservice.beans.SimplifiedInss;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class SimplifiedInssDeserializer extends JsonDeserializer<SimplifiedInss> {

    @Override
    public SimplifiedInss deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String inss = jsonParser.getText();
        if (inss == null) {
            return null;
        }
        return new SimplifiedInss(inss);
    }
}
