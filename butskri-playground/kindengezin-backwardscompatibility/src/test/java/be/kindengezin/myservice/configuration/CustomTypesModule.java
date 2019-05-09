package be.kindengezin.myservice.configuration;

import be.kindengezin.myservice.beans.SimplifiedInss;
import be.kindengezin.myservice.events.SomeId;
import be.kindengezin.myservice.events.SomeOtherId;
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
