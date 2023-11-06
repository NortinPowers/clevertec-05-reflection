package by.clevertec.reflection.converter;

import static by.clevertec.reflection.util.Constant.PACKAGE;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JsonSerializer {

    public String transformObjectToJson(Object object) {
        Map<String, Object> objectMap = serializeObject(object);
        return mapToJsonString(objectMap);
    }

    private Map<String, Object> serializeObject(Object object) {
        Map<String, Object> serializedObject = new HashMap<>();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(object);
                if (value != null) {
                    if (value instanceof LocalDateTime) {
                        value = value.toString();
                    } else if (value instanceof Map) {
                        value = serializeMap((Map<?, ?>) value);
                    } else if (value instanceof List) {
                        value = serializeList((List<?>) value);
                    } else if (value.getClass().getPackage().getName().startsWith(PACKAGE)) {
                        value = serializeObject(value);
                    }
                    serializedObject.put(field.getName(), value);
                }
            } catch (IllegalAccessException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        return serializedObject;
    }

    private List<Object> serializeList(List<?> list) {
        List<Object> serializedList = new ArrayList<>();
        for (Object item : list) {
            if (item instanceof Map) {
                serializedList.add(serializeMap((Map<?, ?>) item));
            } else if (item instanceof List) {
                serializedList.add(serializeList((List<?>) item));
            } else if (item.getClass().getPackage().getName().startsWith(PACKAGE)) {
                serializedList.add(serializeObject(item));
            } else {
                serializedList.add(item);
            }
        }
        return serializedList;
    }

    private Map<String, Object> serializeMap(Map<?, ?> map) {
        Map<String, Object> serializedMap = new HashMap<>();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            if (key instanceof String) {
                if (value instanceof Map) {
                    serializedMap.put((String) key, serializeMap((Map<?, ?>) value));
                } else if (value instanceof List) {
                    serializedMap.put((String) key, serializeList((List<?>) value));
                } else if (value.getClass().getPackage().getName().startsWith(PACKAGE)) {
                    serializedMap.put((String) key, serializeObject(value));
                } else {
                    serializedMap.put((String) key, value);
                }
            }
        }
        return serializedMap;
    }

    private String mapToJsonString(Map<String, Object> map) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{");
        Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            jsonBuilder.append("\"").append(entry.getKey()).append("\":");
            appendValueToJson(entry.getValue(), jsonBuilder);
            if (iterator.hasNext()) {
                jsonBuilder.append(",");
            }
        }
        jsonBuilder.append("}");
        return jsonBuilder.toString();
    }

    private void appendValueToJson(Object value, StringBuilder jsonBuilder) {
        if (value instanceof String) {
            jsonBuilder.append("\"").append(escapeString((String) value)).append("\"");
        } else if (value instanceof Number || value instanceof Boolean) {
            jsonBuilder.append(value);
        } else if (value instanceof Map) {
            jsonBuilder.append(mapToJsonString((Map<String, Object>) value));
        } else if (value instanceof List) {
            jsonBuilder.append(listToJsonString((List<Object>) value));
        } else {
            jsonBuilder.append("null");
        }
    }

    private String listToJsonString(List<Object> list) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[");
        Iterator<Object> iterator = list.iterator();
        while (iterator.hasNext()) {
            Object value = iterator.next();
            appendValueToJson(value, jsonBuilder);
            if (iterator.hasNext()) {
                jsonBuilder.append(",");
            }
        }
        jsonBuilder.append("]");
        return jsonBuilder.toString();
    }

    private String escapeString(String value) {
        StringBuilder escapedBuilder = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c == '"' || c == '\\' || c == '/') {
                escapedBuilder.append('\\');
            }
            escapedBuilder.append(c);
        }
        return escapedBuilder.toString();
    }
}
