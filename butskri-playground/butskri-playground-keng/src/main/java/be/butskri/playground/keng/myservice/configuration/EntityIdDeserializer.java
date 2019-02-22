package be.butskri.playground.keng.myservice.configuration;

import be.butskri.playground.keng.commons.ids.EntityId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class EntityIdDeserializer<T extends EntityId> extends JsonDeserializer<T> {

    private Class<T> clazz;
    private Constructor<T> constructor;

    public static <T extends EntityId> EntityIdDeserializer<T> forClass(Class<T> clazz) {
        return new EntityIdDeserializer<>(clazz);
    }

    public EntityIdDeserializer(Class<T> clazz) {
        this.clazz = clazz;
        try {
            constructor = clazz.getConstructor(UUID.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(String.format("Could not find constructor with UUID parameter for class %s", clazz), e);
        }
    }

    @Override
    public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String id = jsonParser.getText();
        if (id == null) {
            return null;
        }
        try {
            return constructor.newInstance(UUID.fromString(id));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(String.format("Could not create isntance of class %s with UUID parameter %s", clazz, id), e);
        }
    }
}
