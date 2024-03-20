package main.models;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import main.format.converter.CoordinatesConverter;
import main.format.converter.LocationToConverter;
import main.format.converter.LocationFromConverter;
import main.format.converter.LocalTimeConverter;
import main.utils.Validatable;
import main.utils.InvalidValueException;

import java.util.Objects;

public class Route implements Comparable<Route>, Validatable{
    @CsvBindByName(required = true)
    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    @CsvBindByName(required = true)
    private String name; //Поле не может быть null, Строка не может быть пустой
    @CsvCustomBindByName(converter = CoordinatesConverter.class)
    private Coordinates coordinates; //Поле не может быть null
    @CsvCustomBindByName(converter = LocalTimeConverter.class)
    private java.time.LocalDateTime creationDate = java.time.LocalDateTime.now(); //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    @CsvCustomBindByName(converter = LocationFromConverter.class)
    private LocationFrom from; //Поле может быть null
    @CsvCustomBindByName(converter = LocationToConverter.class)
    private LocationTo to; //Поле не может быть null
    @CsvBindByName(required = true)
    private Long distance; //Поле не может быть null, Значение поля должно быть больше 1

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id <= 0) throw new InvalidValueException("id", "значение поля должно быть больше 0");
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null) throw new InvalidValueException("name", "поле не может быть null");
        if (name.isBlank()) throw new InvalidValueException("name", "строка не может быть пустой");
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        if (coordinates == null) throw new InvalidValueException("coordinates", "поле не может быть null");
        this.coordinates = coordinates;
    }

    public LocationFrom getFrom() {
        return from;
    }

    public void setFrom(LocationFrom from) {
        if (from == null) throw new InvalidValueException("from", "поле не может быть null");
        this.from = from;
    }

    public LocationTo getTo() {
        return to;
    }

    public void setTo(LocationTo to) {
        if (to == null) throw new InvalidValueException("to", "поле не может быть null");
        this.to = to;
    }

    public Long getDistance() {
        return distance;
    }

    public void setDistance(Long distance) {
        if (distance <= 0) throw new InvalidValueException("distance", "значение поля должно быть больше 0");
        this.distance = distance;
    }

    @Override
    public int compareTo(Route o) {
        return Long.compare(this.distance, o.distance);
    }

    @Override
    public String toString() {
        return "Route{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", from=" + from +
                ", to=" + to +
                ", distance=" + distance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return id == route.id && name.equals(route.name) && coordinates.equals(route.coordinates) && creationDate.equals(route.creationDate) && from.equals(route.from) && to.equals(route.to) && distance.equals(route.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, from, to, distance);
    }

    @Override
    public void validate() {
        if (id <= 0) throw new InvalidValueException("id", "значение поля должно быть больше 0");
        if (name == null) throw new InvalidValueException("name", "поле не может быть null");
        if (name.isBlank()) throw new InvalidValueException("name", "строка не может быть пустой");
        if (coordinates == null) throw new InvalidValueException("coordinates", "поле не может быть null");
        if (creationDate == null) throw new InvalidValueException("creationDate", "поле не может быть null");
        if (from == null) throw new InvalidValueException("from", "поле не может быть null");
        if (to == null) throw new InvalidValueException("to", "поле не может быть null");
        if (distance <= 0) throw new InvalidValueException("distance", "значение поля должно быть больше 0");
    }
}
