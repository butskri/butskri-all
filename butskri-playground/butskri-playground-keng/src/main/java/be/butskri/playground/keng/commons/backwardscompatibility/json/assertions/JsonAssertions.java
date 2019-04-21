package be.butskri.playground.keng.commons.backwardscompatibility.json.assertions;

import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonAssertions {

    public static void assertJsonSame(String description, String expectedJson, String actualJson) {
        try {
            JSONAssert.assertEquals(expectedJson, actualJson, true);
        } catch (JSONException e) {
            throw new AssertionError("parse error while reading json", e);
        } catch (AssertionError error) {
            assertThat(actualJson)
                    .describedAs(description)
                    .isEqualTo(expectedJson);
        }
    }
}
