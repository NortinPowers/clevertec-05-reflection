package by.clevertec.reflection.converter;

import static by.clevertec.reflection.converter.JsonDeserializer.transformJsonToObject;
import static by.clevertec.reflection.util.Constant.InnerField.DATA_FORMAT;

import by.clevertec.reflection.entity.Inner;
import by.clevertec.reflection.entity.Other;
import by.clevertec.reflection.entity.Second;
import by.clevertec.reflection.entity.TestSome;
import by.clevertec.reflection.util.ClassBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class JsonDeserializerTestSome {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final ClassBuilder classBuilder = ClassBuilder.builder().build();

    static {
        OBJECT_MAPPER.registerModule(new JavaTimeModule()
                .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATA_FORMAT))));
    }

    @Test
    void transformJsonToObjectReturnValidInner_whenTransformJson() throws JsonProcessingException, InvocationTargetException, NoSuchMethodException {
        Inner expected = classBuilder.buildInner();
        String json = OBJECT_MAPPER.writeValueAsString(expected);

        Inner actual = (Inner) transformJsonToObject(json, Inner.class);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void transformJsonToObjectReturnValidTestSome_whenTransformJson() throws JsonProcessingException, InvocationTargetException, NoSuchMethodException {
        TestSome expected = classBuilder.buildTest();
        String json = OBJECT_MAPPER.writeValueAsString(expected);

        TestSome actual = (TestSome) transformJsonToObject(json, TestSome.class);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void transformJsonToObjectReturnValidOther_whenTransformJson() throws JsonProcessingException, InvocationTargetException, NoSuchMethodException {
        Other expected = classBuilder.buildOther();
        String json = OBJECT_MAPPER.writeValueAsString(expected);
        System.out.println(json);

        Other actual = (Other) transformJsonToObject(json, Other.class);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void transformJsonToObjectReturnValidSecond_whenTransformJson() throws JsonProcessingException, InvocationTargetException, NoSuchMethodException {
        Second expected = classBuilder.buildSecond();
        String json = OBJECT_MAPPER.writeValueAsString(expected);

        Second actual = (Second) transformJsonToObject(json, Second.class);

        Assertions.assertEquals(expected, actual);
    }
}
