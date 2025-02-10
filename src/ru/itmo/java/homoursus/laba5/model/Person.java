package ru.itmo.java.homoursus.laba5.model;

import ru.itmo.java.homoursus.laba5.model.modelEnums.Color;
import ru.itmo.java.homoursus.laba5.model.modelEnums.Country;

/**
 * Record, хранящий какое-либо лицо. Реализует интерфейс Comparable для сортировки.
 * Используется в модели.
 * @see Dragon
 * @param name Не может быть null, Строка не может быть пустой
 * @param height Значение должно быть больше 0
 * @param eyeColor Не может быть null
 * @param hairColor Не может быть null
 * @param location Не может быть null
 * @param nationality Не может быть null
 * @version 1.0
 */
public record Person(String name, int height, Color eyeColor, Color hairColor, Country nationality, Location location)
implements Comparable<Person> {

    @Override
    public int compareTo(Person o) {
        return name.compareTo(o.name());
    }
}
