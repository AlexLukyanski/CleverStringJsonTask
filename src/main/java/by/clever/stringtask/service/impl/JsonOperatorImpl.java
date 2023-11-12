package by.clever.stringtask.service.impl;

import by.clever.stringtask.exception.JsonParserException;
import by.clever.stringtask.service.DateTimeConverter;
import by.clever.stringtask.service.JsonOperator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JsonOperatorImpl implements JsonOperator {

    private final static DateTimeConverter dateTimeConverter = new DateTimeConverterImpl();

    @Override
    public <T> String toJson(T t) {

        StringBuilder jsonString = new StringBuilder("{")
                .append("\n");
        try {
            Class<?> classObj = t.getClass();
            Field[] fields = classObj.getDeclaredFields();

            for (Field field : fields) {

                field.setAccessible(true);
                Object fieldObject = field.get(t);

                if (fieldObject instanceof Number) {
                    numberToJson(jsonString, field, t);
                } else if (fieldObject instanceof Collection<?>) {
                    collectionToJson(jsonString, field, fieldObject);
                } else if (fieldObject instanceof Temporal) {
                    temporalObjectToJson(jsonString, field, t);
                } else {
                    commonObjectToJson(jsonString, field, t);
                }
            }

            return jsonString
                    .deleteCharAt(jsonString.length() - 2)
                    .append("}")
                    .toString();

        } catch (IllegalAccessException e) {
            throw new JsonParserException(e);
        }
    }

    @Override
    public Object fromJson(String string, Class<?> type) {

        Map<String, Object> fieldsMap = new LinkedHashMap<>();
        Object o;

        try {
            parseObject(fieldsMap, string);
            o = createObject(fieldsMap, type);
            return o;
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException | NoSuchFieldException e) {
            throw new JsonParserException(e);
        }
    }

    private <T> Object createObject(Map<String, Object> fieldsMap, Class<T> type) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {

        Constructor<?>[] constructors = type.getConstructors();
        Type[] types = constructors[0].getGenericParameterTypes();
        Field[] fields = type.getDeclaredFields();

        Object[] valuesStrings = fieldsMap.values().toArray();
        Object[] values = new Object[valuesStrings.length];

        for (int i = 0; i < valuesStrings.length; i++) {
            if ("java.util.UUID".equals(types[i].getTypeName())) {
                String id = clearString(valuesStrings[i].toString());
                if ("null".equals(id)) {
                    values[i] = null;
                } else {
                    values[i] = UUID.fromString(id);
                }
            } else if ("java.time.LocalDate".equals(types[i].getTypeName())) {
                String date = clearString(valuesStrings[i].toString());
                if ("null".equals(date)) {
                    values[i] = null;
                } else {
                    values[i] = LocalDate.parse(date);
                }
            } else if ("java.lang.Double".equals(types[i].getTypeName())) {
                String number = clearString(valuesStrings[i].toString());
                if ("null".equals(number)) {
                    values[i] = null;
                } else {
                    values[i] = Double.valueOf(number);
                }
            } else if ("int".equals(types[i].getTypeName())) {
                String number = clearString(valuesStrings[i].toString());
                if ("null".equals(number)) {
                    values[i] = null;
                } else {
                    values[i] = Integer.valueOf(number);
                }
            } else if ("java.time.OffsetDateTime".equals(types[i].getTypeName())) {
                String temporalValue = clearString(valuesStrings[i].toString());
                if ("null".equals(temporalValue)) {
                    values[i] = null;
                } else {
                    String convertedTemporalValue = dateTimeConverter.convertFromJson(temporalValue);
                    values[i] = OffsetDateTime.parse(convertedTemporalValue);
                }
            } else if (types[i].getTypeName().contains("List")
                    || types[i].getTypeName().contains("Set")) {
                String value = clearString(valuesStrings[i].toString());
                if ("null".equals(value)) {
                    values[i] = null;
                } else {
                    Field field = fields[i];
                    ParameterizedType paramType = (ParameterizedType) field.getGenericType();
                    Class<?> classType = (Class<?>) paramType.getActualTypeArguments()[0];
                    values[i] = createCollection(valuesStrings[i], classType);
                }
            } else {
                if (valuesStrings[i] == null) {
                    values[i] = null;
                } else {
                    values[i] = clearString(valuesStrings[i].toString());
                }
            }
        }
        Object o = constructors[0].newInstance(values);
        return o;
    }

    private <T> List<T> createCollection(Object object, Class<T> type) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, NoSuchFieldException {

        List<Object> list = (List<Object>) object;
        List<T> collection = new ArrayList();

        for (Object o : list) {
            Map<String, Object> fieldsMap = (Map<String, Object>) o;
            T result = (T) createObject(fieldsMap, type);
            collection.add(result);
        }
        return collection;
    }

    private String clearString(String string) {

        return string.replace("[", "")
                .replace("]", "");
    }

    private void parseObject(Map<String, Object> fieldsMap, String string) {

        boolean isKey = true;

        String key = "";
        List<String> values = new ArrayList<>();

        StringBuilder keyBuilder = new StringBuilder();
        StringBuilder valueBuilder = new StringBuilder();

        char[] chars = string.toCharArray();

        for (int i = 1; i < chars.length; i++) {

            if (":".equals(String.valueOf(chars[i]))) {
                key = clearifyAndReturnString(keyBuilder);
                keyBuilder = new StringBuilder();
                isKey = false;
            }
            if (",".equals(String.valueOf(chars[i]))
                    || "}".equals(String.valueOf(chars[i]))) {
                isKey = true;

                values.add(clearifyAndReturnString(valueBuilder));
                fieldsMap.put(key, values);

                values = new ArrayList<>();
                valueBuilder = new StringBuilder();
            }
            if (isKey) {
                keyBuilder.append(chars[i]);
            }
            if (!isKey) {
                valueBuilder.append(chars[i]);
            }
            if ("[".equals(String.valueOf(chars[i]))) {

                List<Map<String, Object>> result = new ArrayList<>();
                int index = parseArray(string, i + 1, result);
                fieldsMap.put(key, result);
                i = index - 1;
                isKey = true;
            }
        }
    }

    private int parseArray(String string, int j, List<Map<String, Object>> result) {

        char[] chars = string.toCharArray();

        Map<String, Object> fieldsMap = new LinkedHashMap<>();

        boolean isKey = true;

        String key = "";
        List<String> values = new ArrayList<>();

        int count = 0;

        StringBuilder keyBuilder = new StringBuilder();
        StringBuilder valueBuilder = new StringBuilder();

        for (int i = j; i < chars.length; i++) {

            count++;

            if (":".equals(String.valueOf(chars[i]))) {
                isKey = false;
            }
            if (",".equals(String.valueOf(chars[i]))
                    || "}".equals(String.valueOf(chars[i]))) {
                isKey = true;
                key = clearifyAndReturnString(keyBuilder);

                values.add(clearifyAndReturnString(valueBuilder));
                fieldsMap.put(key, values);

                values = new ArrayList<>();
                valueBuilder = new StringBuilder();
                keyBuilder = new StringBuilder();
            }
            if (isKey) {
                keyBuilder.append(chars[i]);

            }
            if (!isKey) {
                valueBuilder.append(chars[i]);
            }
            if ("}".equals(String.valueOf(chars[i]))) {
                isKey = true;
                result.add(fieldsMap);
                fieldsMap = new LinkedHashMap<>();
            }
            if ("[".equals(String.valueOf(chars[i]))) {
                List<Map<String, Object>> result2 = new ArrayList<>();
                int index = parseArray(string, i + 1, result2);
                i = index - 1;
            }

            if ("]".equals(String.valueOf(chars[i]))) {
                break;
            }
        }
        return j + count;
    }

    private String clearifyAndReturnString(StringBuilder stringBuilder) {

        String clearString = stringBuilder.toString()
                .replace(":", "")
                .replace("}", "")
                .replace("{", "")
                .replace("[", "")
                .replace("]", "")
                .replace(",", "")
                .replace(" ", "")
                .replace("\"", "")
                .replace("\n", "");

        return clearString;
    }

    private <T> void commonObjectToJson(StringBuilder jsonString, Field field, T t) throws IllegalAccessException {

        String fieldName = field.getName();
        Object fieldValue = field.get(t);

        if (fieldValue == null) {
            jsonString.append("\"")
                    .append(fieldName)
                    .append("\"")
                    .append(": ")
                    .append("null")
                    .append(",")
                    .append("\n");
        } else {
            jsonString.append("\"")
                    .append(fieldName)
                    .append("\"")
                    .append(": ")
                    .append("\"")
                    .append(fieldValue)
                    .append("\"")
                    .append(",")
                    .append("\n");
        }
    }

    private <T> void temporalObjectToJson(StringBuilder jsonString, Field field, T t) throws IllegalAccessException {

        String fieldName = field.getName();
        Object fieldValue = field.get(t);

        if (fieldValue == null) {
            jsonString.append("\"")
                    .append(fieldName)
                    .append("\"")
                    .append(": ")
                    .append("null")
                    .append(",")
                    .append("\n");
        } else {
            String convertedTemporal = dateTimeConverter.convertToJson(fieldValue.toString());
            jsonString.append("\"")
                    .append(fieldName)
                    .append("\"")
                    .append(": ")
                    .append("\"")
                    .append(convertedTemporal)
                    .append("\"")
                    .append(",")
                    .append("\n");
        }
    }

    private <T> void numberToJson(StringBuilder jsonString, Field field, T t) throws IllegalAccessException {

        String fieldName = field.getName();
        Object fieldValue = field.get(t);

        if (fieldValue == null) {
            jsonString.append("\"")
                    .append(fieldName)
                    .append("\"")
                    .append(": ")
                    .append("null")
                    .append(",")
                    .append("\n");
        } else {
            jsonString.append("\"")
                    .append(fieldName)
                    .append("\"")
                    .append(": ")
                    .append(fieldValue)
                    .append(",")
                    .append("\n");
        }
    }

    private void collectionToJson(StringBuilder jsonString, Field field, Object fieldObject) {

        jsonString.append("\"")
                .append(field.getName())
                .append("\"")
                .append(": ")
                .append("[")
                .append("\n");

        Collection<?> collection = (Collection<?>) fieldObject;

        for (Object o : collection) {
            String string = toJson(o);
            jsonString.append(string)
                    .append("\n");
        }

        jsonString.append("]")
                .append("\n");
    }
}