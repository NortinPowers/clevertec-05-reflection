package by.clevertec.reflection.converter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;

@UtilityClass
public class JsonDeserializer {

    public static Object transformJsonToObject(String jsonString, Class<?> clazz) throws InvocationTargetException, NoSuchMethodException {
        Map<String, Object> objectMap = parseJson(jsonString);
        return deserializeObject(objectMap, clazz);
    }

    private static Map<String, Object> parseJson(String jsonString) {
        Map<String, Object> jsonMap = new LinkedHashMap<>();
        Pattern objectPattern = Pattern.compile("\\{[^{}]*\\}");
        Pattern keyValuePairPattern = Pattern.compile("\"(\\w+)\":([^,}]+)");
        Matcher objectMatcher = objectPattern.matcher(jsonString);
        while (objectMatcher.find()) {
            String object = objectMatcher.group();
            Matcher keyValuePairMatcher = keyValuePairPattern.matcher(object);
            Map<String, Object> innerMap = new LinkedHashMap<>();
            while (keyValuePairMatcher.find()) {
                String key = keyValuePairMatcher.group(1);
                String value = keyValuePairMatcher.group(2);
                innerMap.put(key, parseValue(value));
            }
            jsonMap.putAll(innerMap);
        }
        return jsonMap;
    }

    private static Object parseValue(String value) {
        if (value.startsWith("{")) {
            return parseJson(value);
        } else if (value.startsWith("[")) {
            return parseJsonArray(value);
        } else if (value.startsWith("\"")) {
            return value.substring(1, value.length() - 1);
        } else if ("true".equals(value)) {
            return true;
        } else if ("false".equals(value)) {
            return false;
        } else if (value.matches("-?\\d+(\\.\\d+)?")) {
            if (value.contains(".")) {
                return Double.parseDouble(value);
            } else {
                return Long.parseLong(value);
            }
        }
        return null;
    }

    private static List<Object> parseJsonArray(String jsonArrayString) {
        List<Object> jsonArray = new ArrayList<>();
        Pattern objectPattern = Pattern.compile("\\{[^{}]*\\}");
        Matcher objectMatcher = objectPattern.matcher(jsonArrayString);
        while (objectMatcher.find()) {
            Map<String, Object> innerMap = parseJson(objectMatcher.group());
            jsonArray.add(innerMap);
        }
        return jsonArray;
    }

    private static Object deserializeObject(Map<String, Object> objectMap, Class<?> clazz)
            throws NoSuchMethodException, InvocationTargetException {
        try {
            Object object = clazz.getDeclaredConstructor().newInstance();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                if (objectMap.containsKey(fieldName)) {
                    Object fieldValue = objectMap.get(fieldName);
                    if (fieldValue != null) {
                        fieldValue = processFieldValue(fieldValue, field.getType());
                        field.set(object, fieldValue);
                    }
                }
            }
            return object;
        } catch (InstantiationException | IllegalAccessException e) {
            System.out.println("log: " + e.getMessage());
            return null;
        }
    }

    private static Object processFieldValue(Object fieldValue, Class<?> fieldType)
            throws NoSuchMethodException, InvocationTargetException {
        if (fieldType.equals(LocalDateTime.class)) {
            return LocalDateTime.parse((CharSequence) fieldValue);
        } else if (fieldType.equals(Map.class)) {
            return deserializeMap((Map<?, ?>) fieldValue);
        } else if (fieldType.equals(Character.class) || fieldType.equals(char.class)) {
            return ((String) fieldValue).charAt(0);
        } else if (fieldType.equals(List.class)) {
            ParameterizedType listType = (ParameterizedType) fieldType.getGenericSuperclass();
            Type elementType = listType.getActualTypeArguments()[0];
            return deserializeList((List<?>) fieldValue, elementType);
        } else if (!fieldType.isPrimitive() && !fieldType.getPackage().getName().startsWith("java.lang")) {
            return deserializeObject((Map<String, Object>) fieldValue, fieldType);
        } else {
            return fieldValue;
        }
    }

    private static List<Object> deserializeList(List<?> list, Type elementType)
            throws NoSuchMethodException, InvocationTargetException {
        List<Object> deserializedList = new ArrayList<>();
        for (Object item : list) {
            if (item instanceof Map && elementType instanceof ParameterizedType parameterizedType) {
                Type typeArgument = parameterizedType.getActualTypeArguments()[0];
                deserializedList.add(deserializeObject((Map<String, Object>) item, (Class<?>) typeArgument));
            } else {
                deserializedList.add(item);
            }
        }
        return deserializedList;
    }

    private static List<Object> deserializeList(List<?> list)
            throws NoSuchMethodException, InvocationTargetException {
        List<Object> deserializedList = new ArrayList<>();
        for (Object item : list) {
            if (item instanceof Map) {
                deserializedList.add(deserializeObject((Map<String, Object>) item, Object.class));
            } else if (item instanceof List) {
                deserializedList.add(deserializeList((List<?>) item));
            } else {
                deserializedList.add(item);
            }
        }
        return deserializedList;
    }

    private static Map<String, ?> deserializeMap(Map<?, ?> map)
            throws NoSuchMethodException, InvocationTargetException {
        Map<String, Object> deserializedMap = new HashMap<>();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            String key = entry.getKey().toString();
            Object value = entry.getValue();
            if (value instanceof Map) {
                value = deserializeMap((Map<?, ?>) value);
            } else if (value instanceof List) {
                value = deserializeList((List<?>) value);
            }
            deserializedMap.put(key, value);
        }
        return deserializedMap;
    }
}
