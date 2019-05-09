package be.kindengezin.backwardscompatibility.json.random;

import io.github.benas.randombeans.EnhancedRandomBuilder;

import static java.nio.charset.Charset.forName;

public class RandomizationTestConstants {

    private static final int DEFAULT_MIN_STRING_LENGTH = 5;
    private static final int DEFAULT_MAX_STRING_LENGTH = 50;
    private static final int DEFAULT_MIN_COLLECTION_SIZE = 2;
    private static final int DEFAULT_MAX_COLLECTION_SIZE = 2;
    private static final int RANDOMIZATION_DEPTH = 5;

    public static EnhancedRandomBuilder baseEnhancedRandomBuilder() {
        return EnhancedRandomBuilder.aNewEnhancedRandomBuilder()
                .randomizationDepth(RANDOMIZATION_DEPTH)
                .charset(forName("UTF-8"))
                .stringLengthRange(DEFAULT_MIN_STRING_LENGTH, DEFAULT_MAX_STRING_LENGTH)
                .collectionSizeRange(DEFAULT_MIN_COLLECTION_SIZE, DEFAULT_MAX_COLLECTION_SIZE)
                .scanClasspathForConcreteTypes(true)
                .overrideDefaultInitialization(true);
    }
}
