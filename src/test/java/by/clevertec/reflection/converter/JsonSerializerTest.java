package by.clevertec.reflection.converter;

import static by.clevertec.reflection.converter.JsonSerializer.transformObjectToJson;
import static by.clevertec.reflection.util.BaseBuilder.getBase;
import static by.clevertec.reflection.util.BaseBuilder.getJsonBase;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class JsonSerializerTest {

    @Test
    void transformObjectToJsonShouldResultEquals_whenComparingObjectToJson() {
        String expected = getJsonBase();

        String actual = transformObjectToJson(getBase());

        Assertions.assertEquals(expected, actual);
    }
}
