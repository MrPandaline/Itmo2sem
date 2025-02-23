package laba5.model;

import laba5.model.modelEnums.Color;
import laba5.model.modelEnums.Country;

/**
 * Класс, хранящий какое-либо лицо. Реализует интерфейс Comparable для сортировки.
 * Используется в модели.
 * @see Dragon
 * @version 1.0
 */
public class Person extends AbstractModel implements Comparable<Person> {
    /**
     * Имя лица.
     * Не может быть null, Строка не может быть пустой.
     */
    private String name;
    /**
     * Рост лица.
     * Значение должно быть больше 0.
     */
    private int height;
    /**
     * Цвет глаз лица.
     * Не может быть null.
     */
    private Color eyeColor;
    /**
     * Цвет волос лица.
     * Не может быть null.
     */
    private Color hairColor;
    /**
     * Национальность лица.
     * Не может быть null.
     */
    private Country nationality;
    /**
     * Локация, где находится лицо.
     * Не может быть null.
     */
    private Location location;

    public Person(String name, int height, Color eyeColor, Color hairColor, Country nationality, Location location) {
    this.name = name;
    this.height = height;
    this.eyeColor = eyeColor;
    this.hairColor = hairColor;
    this.nationality = nationality;
    this.location = location;
    }

    public Color eyeColor() {
        return eyeColor;
    }

    public String name() {
        return name;
    }

    public Color hairColor() {
        return hairColor;
    }

    public Country nationality() {
        return nationality;
    }

    public int height() {
        return height;
    }

    public Location location() {
        return location;
    }
    @Override
    public int compareTo(Person o) {
        return name.compareTo(o.name());
    }
}