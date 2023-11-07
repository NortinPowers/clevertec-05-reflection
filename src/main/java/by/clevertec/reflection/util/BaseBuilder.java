package by.clevertec.reflection.util;

import by.clevertec.reflection.converter.GsonLocalDateTimeDeserializer;
import by.clevertec.reflection.entity.Base;
import by.clevertec.reflection.entity.Inner;
import by.clevertec.reflection.entity.Second;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BaseBuilder {

    public static Base getBase() {
        Second second = new Second();
        Inner innerOne = new Inner();
        Inner innerTwo = new Inner();
        innerOne.setDateTime(LocalDateTime.of(2023, 11, 6, 20, 55, 25));
        innerOne.setName("inner One");
        innerTwo.setDateTime(LocalDateTime.of(2023, 10, 3, 10, 39, 14));
        innerTwo.setName("inner Two");
        second.setType("second type");
        Map<String, List<Inner>> listMap = new HashMap<>();
        List<Inner> inners = List.of(innerOne, innerTwo);
        listMap.put("innerMapKey", inners);
        second.setListMap(listMap);
        Base base = new Base();
        base.setId(1L);
        base.setSecond(second);
        return base;
    }

    public static String getJsonBase() {
        return "{\"id\":1,\"second\":{\"listMap\":{\"innerMapKey\":[{\"dateTime\":\"2023-11-06T20:55:25\",\"name\":\"inner One\"},{\"dateTime\":\"2023-10-03T10:39:14\",\"name\":\"inner Two\"}]},\"type\":\"second type\"}}";
    }

    public static Base getBaseThroughGson() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeDeserializer())
                .create();
        return gson.fromJson(getJsonBase(), Base.class);
    }
}
