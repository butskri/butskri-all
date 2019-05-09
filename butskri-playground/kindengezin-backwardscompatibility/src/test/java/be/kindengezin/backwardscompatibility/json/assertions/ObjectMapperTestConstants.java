package be.kindengezin.backwardscompatibility.json.assertions;

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

public class ObjectMapperTestConstants {

    public static ObjectMapper objectMapperForTests() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper = new ObjectMapper();
        objectMapper.addMixIn(SuperBean.class, SuperBeanMixin.class);
        objectMapper.registerModules(new DateTimeSerializationModule(), new CustomTypesModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        return objectMapper;
    }
}
