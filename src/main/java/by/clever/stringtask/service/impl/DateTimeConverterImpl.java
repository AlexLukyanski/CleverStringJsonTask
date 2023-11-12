package by.clever.stringtask.service.impl;

import by.clever.stringtask.service.DateTimeConverter;

public class DateTimeConverterImpl implements DateTimeConverter {

    @Override
    public String convertToJson(String dateTime) {
        if (dateTime.contains(":")) {
            return dateTime.replace(":", "---");
        }
        return dateTime;
    }

    @Override
    public String convertFromJson(String dateTime) {
        if (dateTime.contains("---")) {
            return dateTime.replace("---", ":");
        }
        return dateTime;
    }
}
