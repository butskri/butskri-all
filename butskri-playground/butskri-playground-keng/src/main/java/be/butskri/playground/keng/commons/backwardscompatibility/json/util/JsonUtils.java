package be.butskri.playground.keng.commons.backwardscompatibility.json.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.fail;

public class JsonUtils {

    public static String loadJson(File file) {
        if (!file.exists()) {
            return null;
        }
        try {
            return FileUtils.readFileToString(file, "UTF-8");
        } catch (IOException e) {
            fail(String.format("Problem reading file %s", file), e);
            return null;
        }
    }

    public static String jsonFor(Object object, ObjectMapper objectMapper) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            fail(String.format("Problem converting object to json %s", object), e);
            return null;
        }
    }

    public static void writeJsonToFile(Object object, File file, ObjectMapper objectMapper) {
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, object);
        } catch (IOException e) {
            fail(String.format("Problem writing object %s of type %s. Could not write json to file %s", object, object.getClass(), file), e);
        }
    }

}
