package se.anders_raberg.proptest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TrailingTrimPropertiesTest {

    static Stream<Arguments> testProperties() {
        return Stream.of(Arguments.of("alpha", "test", "test"), Arguments.of("bravo", "test2\t\t", "test2"),
                Arguments.of("charlie", "test3  ", "test3"));
    }

    /* @formatter:off */
    final String testFile = """
            a = b       
            c = d       
            x = y
            """;
    /* @formatter:on */

    private TrailingTrimProperties testee;

    @BeforeEach
    void setUp() {
        testee = new TrailingTrimProperties();
    }

    @ParameterizedTest
    @MethodSource("testProperties")
    void testPut(String key, String value, String trimmedValue) {
        testee.put(key, value);
        assertEquals(trimmedValue, testee.get(key));
    }

    @ParameterizedTest
    @MethodSource("testProperties")
    void testPutIfAbsent(String key, String value, String trimmedValue) {
        testee.put(key, value);
        assertEquals(trimmedValue, testee.get(key));
    }

    @Test
    void testPutAll() {
        Map<Object, Object> orig = testProperties().collect(Collectors.toMap(a -> a.get()[0], a -> a.get()[1]));
        Map<Object, Object> expectedResult = testProperties()
                .collect(Collectors.toMap(a -> a.get()[0], a -> a.get()[2]));
        testee.putAll(orig);
        Map<Object, Object> actualResult = testee.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void testLoad() throws IOException {
        testee.load(new ByteArrayInputStream(testFile.getBytes(StandardCharsets.UTF_8)));
    }

}