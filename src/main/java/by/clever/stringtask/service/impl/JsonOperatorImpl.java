package by.clever.stringtask.service.impl;

import by.clever.stringtask.service.JsonOperator;

import java.lang.reflect.Field;
import java.util.Collection;

public class JsonOperatorImpl implements JsonOperator {

    @Override
    public String toJson(Object object) {

        StringBuilder jsonString = new StringBuilder("{")
                .append("\n");
        try {
            Class<?> classObj = object.getClass();
            Field[] fields = classObj.getDeclaredFields();

            for (Field field : fields) {

                field.setAccessible(true);
                Object fieldObject = field.get(object);

                if (fieldObject instanceof Number) {
                    numberToJson(jsonString, field, object);
                } else if (fieldObject instanceof Collection<?>) {
                    collectionToJson(jsonString, field, fieldObject);
                } else {
                    commonObjectToJson(jsonString, field, object);
                }
            }

            return jsonString
                    .deleteCharAt(jsonString.length() - 2)
                    .append("}")
                    .toString();

        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object fromJson(String string) {
        return null;
    }

    private void commonObjectToJson(StringBuilder jsonString, Field field, Object object) throws IllegalAccessException {
        String fieldName = field.getName();
        Object fieldValue = field.get(object);
        jsonString.append("\"")
                .append(fieldName)
                .append("\"")
                .append(": ")
                .append("\"")
                .append(fieldValue.toString())
                .append("\"")
                .append(",")
                .append("\n");
    }

    private void numberToJson(StringBuilder jsonString, Field field, Object object) throws IllegalAccessException {
        String fieldName = field.getName();
        Object fieldValue = field.get(object);
        jsonString.append("\"")
                .append(fieldName)
                .append("\"")
                .append(": ")
                .append(fieldValue.toString())
                .append(",")
                .append("\n");
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



