package main.format.converter;

import com.opencsv.bean.AbstractBeanField;
import main.models.LocationFrom;

public class LocationFromConverter extends AbstractBeanField<LocationFrom, String> {
    @Override
    protected LocationFrom convert(String value) {
        if (value == null || value.isEmpty())
            return null;

        String[] parts = value.split(",");
        if (parts.length != 3 || !parts[0].matches("\\d+") || !parts[1].matches("\\d+") || !parts[2].matches("\\d+"))
            throw new IllegalArgumentException("Incorrect location format");

        LocationFrom locationFrom = new LocationFrom();

        locationFrom.setX(Long.parseLong(parts[0]));
        locationFrom.setY(Integer.parseInt(parts[1]));
        locationFrom.setName(parts[2]);

        return locationFrom;
    }

    @Override
    public String convertToWrite(Object value) {
        LocationFrom locationFrom = (LocationFrom) value;
        if (locationFrom == null)
            return "";
        return locationFrom.getX() + "," + locationFrom.getY() + "," + locationFrom.getName();
    }
}
