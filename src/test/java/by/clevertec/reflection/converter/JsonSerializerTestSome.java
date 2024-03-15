package by.clevertec.reflection.converter;

import static by.clevertec.reflection.converter.JsonSerializer.transformObjectToJson;
import static by.clevertec.reflection.util.BaseBuilder.getBase;
import static by.clevertec.reflection.util.BaseBuilder.getJsonBase;
import static by.clevertec.reflection.util.Constant.InnerField.DATA_FORMAT;
import static org.junit.jupiter.api.Assertions.assertEquals;

import by.clevertec.reflection.util.ClassBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class JsonSerializerTestSome {

    @Test
    void transformObjectToJsonShouldReturnValidString_whenTransformBase() {
        String expected = getJsonBase();

        String actual = transformObjectToJson(getBase());

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource
    void transformObjectToJsonShouldReturnValidString_whenTransformCustomObject(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()
                .addSerializer(
                        LocalDateTime.class,
                        new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATA_FORMAT))));

        String expected = objectMapper.writeValueAsString(object);

        String actual = transformObjectToJson(object);

        assertEquals(expected, actual);
    }

    static Stream<Object> transformObjectToJsonShouldReturnValidString_whenTransformCustomObject() {
        ClassBuilder classBuilder = ClassBuilder.builder().build();
        return Stream.of(
                classBuilder.buildInner(),
                classBuilder.buildTest(),
                classBuilder.buildListOfInner(),
                classBuilder.buildOther(),
                classBuilder.buildStringInnerMap(),
                classBuilder.buildSecond()
        );
    }
}
