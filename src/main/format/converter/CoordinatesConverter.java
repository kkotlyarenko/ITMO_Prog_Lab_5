package main.format.converter;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import main.models.Coordinates;

public class CoordinatesConverter extends AbstractBeanField<Coordinates, String> {
    @Override
    protected Coordinates convert(String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        if (value == null || value.isEmpty())
            return null;

        String[] parts = value.split(",");
        if (parts.length != 2 || !parts[0].matches("\\d+") || !parts[1].matches("\\d+[.,]\\d+"))
            throw new CsvDataTypeMismatchException("Incorrect coordinates format");

        Coordinates coordinates = new Coordinates();

        coordinates.setX(Long.parseLong(parts[0]));
        coordinates.setY(Double.parseDouble(parts[1]));

        return coordinates;
    }

    @Override
    public String convertToWrite(Object value) {
        Coordinates coordinates = (Coordinates) value;
        if (coordinates == null)
            return "";
        return coordinates.getX() + "," + coordinates.getY();
    }
}
