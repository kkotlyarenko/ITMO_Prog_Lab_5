package main.format.converter;

import com.opencsv.bean.AbstractBeanField;

import java.time.LocalDate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeConverter extends AbstractBeanField<LocalDateTime, String> {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm:ss");

    @Override
    protected LocalDateTime convert(String value) {
        if (value == null || value.isEmpty())
            return null;

        try {
            return LocalDate.parse(value, dateTimeFormatter).atStartOfDay();
        } catch (Exception e) {
            System.err.println("Incorrect data format | dd.MM.yyyy hh:mm:ss");
            e.printStackTrace();
            return LocalDateTime.now();
        }
    }

    @Override
    public String convertToWrite(Object value) {
        LocalDateTime localDate = (LocalDateTime) value;
        if (localDate == null)
            return "";
        return localDate.format(dateTimeFormatter);
    }
}
