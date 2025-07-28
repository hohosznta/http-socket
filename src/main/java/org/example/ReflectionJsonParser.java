package org.example;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ReflectionJsonParser {

    public static <T> T deserialize(String json, Class<T> clazz) throws Exception {
        json = json.trim();
        if (json.startsWith("{")) json = json.substring(1);
        if (json.endsWith("}")) json = json.substring(0, json.length() - 1);

        Map<String, String> map = new HashMap<>(); // JSON을 key-value 쌍으로 파싱
        String[] pairs = json.split(",");

        for (String pair : pairs) {
            String[] kv = pair.split(":");
            if (kv.length != 2) continue;

            String key = kv[0].trim().replaceAll("^\"|\"$", "");
            String value = kv[1].trim().replaceAll("^\"|\"$", "");
            map.put(key, value);
        }

        T obj = clazz.getDeclaredConstructor().newInstance(); //객체 생성

        for (Field field : clazz.getDeclaredFields()) { //모든 필드 가져오기
            field.setAccessible(true);
            String fieldName = field.getName(); // "name" → "age"
            String value = map.get(fieldName); // "Han" → "25"
            if (value == null) continue;

            Class<?> fieldType = field.getType();
            if (fieldType == String.class) {
                field.set(obj, value);  // obj.name = "Han"
            } else if (fieldType == int.class || fieldType == Integer.class) {
                field.set(obj, Integer.parseInt(value));
            } else if (fieldType == long.class || fieldType == Long.class) {
                field.set(obj, Long.parseLong(value));
            } else if (fieldType == boolean.class || fieldType == Boolean.class) {
                field.set(obj, Boolean.parseBoolean(value));
            }
        }

        return obj;
    }
}
