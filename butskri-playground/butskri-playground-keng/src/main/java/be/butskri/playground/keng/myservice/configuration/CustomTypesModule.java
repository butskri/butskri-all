package be.butskri.playground.keng.myservice.configuration;

import be.butskri.playground.keng.myservice.beans.SimplifiedInss;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class CustomTypesModule extends SimpleModule {

    public CustomTypesModule() {
        addSerializer(SimplifiedInss.class, new SimplifiedInssSerializer());
        addDeserializer(SimplifiedInss.class, new SimplifiedInssDeserializer());
    }

}
