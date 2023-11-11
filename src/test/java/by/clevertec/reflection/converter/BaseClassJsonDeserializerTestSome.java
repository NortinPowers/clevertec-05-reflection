package by.clevertec.reflection.converter;

import static by.clevertec.reflection.util.BaseBuilder.getBase;
import static by.clevertec.reflection.util.BaseBuilder.getJsonBase;
import static org.junit.jupiter.api.Assertions.assertEquals;

import by.clevertec.reflection.entity.Base;
import org.junit.jupiter.api.Test;

class BaseClassJsonDeserializerTestSome {

    @Test
    void convertJsonToBaseShouldResultEquals_whenComparingObjectFromJson() {
        BaseClassJsonDeserializer baseClassJsonDeserializer = new BaseClassJsonDeserializer();
        Base expected = getBase();

        Base actual = baseClassJsonDeserializer.convertJsonToBase(getJsonBase());

        assertEquals(expected, actual);
    }
}
