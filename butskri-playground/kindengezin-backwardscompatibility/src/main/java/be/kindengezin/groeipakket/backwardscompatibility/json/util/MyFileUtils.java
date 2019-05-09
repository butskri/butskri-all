package be.kindengezin.groeipakket.backwardscompatibility.json.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.assertj.core.api.Assertions;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.fail;

public class MyFileUtils {

    public static void cleanDirectory(File directory) {
        try {
            if (directory.exists()) {
                FileUtils.cleanDirectory(directory);
            }
        } catch (IOException e) {
            Assertions.fail(String.format("Could not clean directory %s", directory), e);
        }
    }

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

    public static Object loadObject(File file, ObjectMapper objectMapper, Class<?> clazz) {
        if (!file.exists()) {
            return null;
        }
        try {
            return objectMapper.readValue(file, clazz);
        } catch (IOException e) {
            fail(String.format("Problem loading object of type %s. Could not read json from file %s", clazz, file), e);
            return null;
        }
    }

    public static Collection<String> allFileNamesIn(File folder) {
        return Arrays.stream(folder.listFiles())
                .filter(File::isFile)
                .map(File::getName)
                .sorted()
                .collect(Collectors.toList());
    }
}
