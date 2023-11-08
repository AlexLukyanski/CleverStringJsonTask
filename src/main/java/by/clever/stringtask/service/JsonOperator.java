package by.clever.stringtask.service;

public interface JsonOperator {

    String toJson(Object object);
    Object fromJson(String string);

}
