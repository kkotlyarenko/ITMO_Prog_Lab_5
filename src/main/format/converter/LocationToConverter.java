package main.format.converter;

import com.opencsv.bean.AbstractBeanField;
import main.models.LocationTo;

public class LocationToConverter extends AbstractBeanField<LocationTo, String> {
    @Override
    protected LocationTo convert(String value) {
        if (value == null || value.isEmpty())
            return null;

        String[] parts = value.split(",");
        if (parts.length != 3 || !parts[0].matches("\\d+") || !parts[1].matches("\\d+"))
            throw new IllegalArgumentException("Incorrect location format");

        LocationTo locationTo = new LocationTo();

        locationTo.setX(Long.parseLong(parts[0]));
        locationTo.setY(Long.parseLong(parts[1]));
        locationTo.setZ(Double.parseDouble(parts[2]));

        return locationTo;
    }

    @Override
    public String convertToWrite(Object value) {
        LocationTo locationTo = (LocationTo) value;
        if (locationTo == null)
            return "";
        return locationTo.getX() + "," + locationTo.getY() + "," + locationTo.getZ();
    }
}
