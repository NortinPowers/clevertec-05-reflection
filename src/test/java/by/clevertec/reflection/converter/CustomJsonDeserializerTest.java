package by.clevertec.reflection.converter;

import static by.clevertec.reflection.util.BaseBuilder.getBase;
import static by.clevertec.reflection.util.BaseBuilder.getJsonBase;
import static org.junit.jupiter.api.Assertions.assertEquals;

import by.clevertec.reflection.entity.Base;
import org.junit.jupiter.api.Test;

class CustomJsonDeserializerTest {

    @Test
    void convertJsonToBaseShouldResultEquals_whenComparingObjectFromJson() {
        CustomJsonDeserializer customJsonDeserializer = new CustomJsonDeserializer();
        Base expected = getBase();

        Base actual = customJsonDeserializer.convertJsonToBase(getJsonBase());

        assertEquals(expected, actual);
    }
}
