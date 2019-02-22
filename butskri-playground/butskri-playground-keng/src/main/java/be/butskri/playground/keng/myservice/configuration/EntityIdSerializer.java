package be.butskri.playground.keng.myservice.configuration;

import be.butskri.playground.keng.commons.ids.EntityId;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class EntityIdSerializer<T extends EntityId> extends JsonSerializer<T> {

    @Override
    public void serialize(T entityId, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(entityId.getValue().toString());
    }
}
