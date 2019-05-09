package be.kindengezin.groeipakket.backwardscompatibility.json.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.fail;

public class JsonUtils {

    public static String jsonFor(Object object, ObjectMapper objectMapper) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            fail(String.format("Problem converting object to json %s", object), e);
            return null;
        }
    }

}
