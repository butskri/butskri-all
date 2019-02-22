package be.butskri.playground.keng.myservice.configuration;

import be.butskri.playground.keng.myservice.beans.SimplifiedInss;
import be.butskri.playground.keng.myservice.events.SomeId;
import be.butskri.playground.keng.myservice.events.SomeOtherId;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class CustomTypesModule extends SimpleModule {

    public CustomTypesModule() {
        addSerializer(SimplifiedInss.class, new SimplifiedInssSerializer());
        addDeserializer(SimplifiedInss.class, new SimplifiedInssDeserializer());
        addSerializer(SomeId.class, new EntityIdSerializer<>());
        addDeserializer(SomeId.class, EntityIdDeserializer.forClass(SomeId.class));
        addSerializer(SomeOtherId.class, new EntityIdSerializer<>());
        addDeserializer(SomeOtherId.class, EntityIdDeserializer.forClass(SomeOtherId.class));
    }
}
