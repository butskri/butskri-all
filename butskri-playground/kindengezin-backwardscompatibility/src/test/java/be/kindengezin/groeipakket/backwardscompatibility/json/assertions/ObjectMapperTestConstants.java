package be.kindengezin.groeipakket.backwardscompatibility.json.assertions;

import be.kindengezin.myservice.beans.SuperBean;
import be.kindengezin.myservice.configuration.CustomTypesModule;
import be.kindengezin.myservice.configuration.DateTimeSerializationModule;
import be.kindengezin.myservice.configuration.SuperBeanMixin;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;

import java.time.Instant;

public class ObjectMapperTestConstants {

    public static ObjectMapper objectMapperForTests() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper = new ObjectMapper();
        objectMapper.addMixIn(SuperBean.class, SuperBeanMixin.class);
        objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        
        objectMapper.registerModule(timeModule());
        objectMapper.registerModule(new DateTimeSerializationModule());
        objectMapper.registerModule(new CustomTypesModule());

        return objectMapper;
    }

    public static SimpleModule timeModule() {
        SimpleModule timeModule = new SimpleModule();
        timeModule.addDeserializer(Instant.class, InstantDeserializer.INSTANT);
        timeModule.addSerializer(Instant.class, InstantSerializer.INSTANCE);
        return timeModule;
    }
}
