package by.clevertec.reflection.util;

import by.clevertec.reflection.entity.Inner;
import by.clevertec.reflection.entity.Other;
import by.clevertec.reflection.entity.Second;
import by.clevertec.reflection.entity.TestSome;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(setterPrefix = "with")
public class ClassBuilder {

    @Builder.Default
    private String name = "innerOne";

    @Builder.Default
    private LocalDateTime dateTime = LocalDateTime.of(2023, 11, 11, 17, 15, 0);

    @Builder.Default
    private char aChar = 'w';

    @Builder.Default
    private String type = "secondType";

    @Builder.Default
    private int anInt = 10;

    @Builder.Default
    private boolean aBoolean = true;

    public Inner buildInner() {
        return new Inner(name, dateTime);
    }

    public TestSome buildTest() {
        return new TestSome(anInt, aBoolean);
    }

    public Other buildOther() {
        return new Other(aChar, buildTest());
    }

    public List<Inner> buildListOfInner() {
        Inner innerTwo = new Inner("innerTwo", dateTime.plusYears(1).plusMonths(1).minusDays(1));
        return List.of(buildInner(), innerTwo);
    }

    public Map<String, List<Inner>> buildStringInnerMap() {
        return Map.of("mapKey", buildListOfInner());
    }

    public Second buildSecond() {
        return new Second(type, buildStringInnerMap());
    }
}
