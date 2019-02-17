package be.butskri.playground.keng;

import com.fasterxml.jackson.databind.module.SimpleModule;

public class CustomTypesModule extends SimpleModule {

    public CustomTypesModule() {
        addSerializer(SimplifiedInss.class, new SimplifiedInssSerializer());
        addDeserializer(SimplifiedInss.class, new SimplifiedInssDeserializer());
    }

}
