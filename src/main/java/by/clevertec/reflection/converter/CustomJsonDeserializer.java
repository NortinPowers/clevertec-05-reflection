package by.clevertec.reflection.converter;

import static by.clevertec.reflection.util.Constant.BaseField.ID;
import static by.clevertec.reflection.util.Constant.BaseField.SECOND;
import static by.clevertec.reflection.util.Constant.InnerField.DATA_FORMAT;
import static by.clevertec.reflection.util.Constant.InnerField.DATE_TIME;
import static by.clevertec.reflection.util.Constant.InnerField.NAME;
import static by.clevertec.reflection.util.Constant.SecondField.LIST_MAP;
import static by.clevertec.reflection.util.Constant.SecondField.TYPE;

import by.clevertec.reflection.entity.Base;
import by.clevertec.reflection.entity.Inner;
import by.clevertec.reflection.entity.Second;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomJsonDeserializer {

    public Base convertJsonToBase(String jsonString) {
        Base base = new Base();
        try {
            parseId(jsonString, base);
            parseSecond(jsonString, base);
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
        }
        return base;
    }

    private void parseId(String source, Base base) throws NoSuchFieldException, IllegalAccessException {
        String pattern = "\"id\":(\\d+),";
        Matcher matcher = setMatcher(source, pattern);
        if (matcher.find()) {
            Long id = Long.parseLong(matcher.group(1));
            setClassField(base, ID, id);
        }
    }

    private void parseSecond(String source, Base base) throws NoSuchFieldException, IllegalAccessException {
        String pattern = "\"second\":\\{(.+?)\\}";
        Matcher matcher = setMatcher(source, pattern);
        if (matcher.find()) {
            Second second = new Second();
            setClassField(base, SECOND, second);
            parseType(source, second);
            parseArray(source, second);
        }
    }

    private void parseType(String source, Second second) throws NoSuchFieldException, IllegalAccessException {
        String pattern = "\"type\":\"([^\"]+)\"";
        Matcher matcher = setMatcher(source, pattern);
        if (matcher.find()) {
            String type = matcher.group(1);
            setClassField(second, TYPE, type);
        }
    }

    private void parseArray(String source, Second second) throws NoSuchFieldException, IllegalAccessException {
        String pattern = "listMap\":\\s*\\{(?:.*\\}\\s*)*\\}";
        Matcher matcher = setMatcher(source, pattern);
        if (matcher.find()) {
            Map<String, List<Inner>> listMap = new HashMap<>();
            setClassField(second, LIST_MAP, listMap);
            parseListMap(source, listMap);
        }
    }

    private void parseListMap(String source, Map<String, List<Inner>> listMap) throws NoSuchFieldException, IllegalAccessException {
        String pattern = "\"(\\w+)\":\\[(.+?)\\]";
        Matcher matcher = setMatcher(source, pattern);
        while (matcher.find()) {
            String key = matcher.group(1);
            List<Inner> innerList = new ArrayList<>();
            parseInners(matcher.group(2), innerList);
            listMap.put(key, innerList);
        }
    }

    private void parseInners(String source, List<Inner> innerList) throws NoSuchFieldException, IllegalAccessException {
        String pattern = "\\{(.+?)\\}";
        Matcher matcher = setMatcher(source, pattern);
        while (matcher.find()) {
            Inner inner = new Inner();
            parseName(matcher.group(1), inner);
            parseDateTime(matcher.group(1), inner);
            innerList.add(inner);
        }
    }

    private void parseName(String source, Inner inner) throws NoSuchFieldException, IllegalAccessException {
        String pattern = "\"name\":\"([^\"]+)\"";
        Matcher matcher = setMatcher(source, pattern);
        if (matcher.find()) {
            String name = matcher.group(1);
            setClassField(inner, NAME, name);
        }
    }

    private void parseDateTime(String source, Inner inner) throws NoSuchFieldException, IllegalAccessException {
        String pattern = "\"dateTime\":\"([^\"]+)\"";
        Matcher matcher = setMatcher(source, pattern);
        if (matcher.find()) {
            String dateTimeStr = matcher.group(1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATA_FORMAT);
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, formatter);
            setClassField(inner, DATE_TIME, dateTime);
        }
    }

    private Field getField(Object obj, String fieldName) throws NoSuchFieldException {
        if (obj == null) {
            return null;
        }
        return obj.getClass().getDeclaredField(fieldName);
    }

    private <T> void setClassField(Object object, String fieldName, T value) throws NoSuchFieldException, IllegalAccessException {
        Field field = getField(object, fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }

    private Matcher setMatcher(String jsonString, String pattern) {
        Pattern correntPattern = Pattern.compile(pattern);
        return correntPattern.matcher(jsonString);
    }
}
