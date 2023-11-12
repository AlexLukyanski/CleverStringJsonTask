package by.clever.stringtask.service;

public interface JsonOperator {

    <T> String toJson(T t);

    Object fromJson(String string, Class<?> type);

}
