package ru.itmo.java.homoursus.laba5.model;

import ru.itmo.java.homoursus.laba5.model.modelEnums.DragonCharacter;
import ru.itmo.java.homoursus.laba5.model.modelEnums.DragonType;

/**
 * Record, хранящий дракона. Он содержится в коллекции реализует интерфейс Comparable.
 * @param age Значение должно быть больше 0
 * @param coordinates Не может быть null
 * @param creationDate Не может быть null, Значение этого поля должно генерироваться автоматически
 * @param description Может быть null
 * @param dragonCharacter Не может быть null
 * @param dragonType Может быть null
 * @param id Значение должно быть больше 0, Значение должно быть уникальным, Значение должно генерироваться автоматически
 * @param name Не может быть null, Строка не может быть пустой
 * @param killer Может быть null
 * @version 1.0
 */
public record Dragon(String name, Coordinates coordinates, long age, String description,
                     DragonType dragonType, DragonCharacter dragonCharacter, Person killer,
                     long id, java.time.ZonedDateTime creationDate) implements Comparable<Dragon> {

    /** Генератор id (генерирует просто последовательные числа, начиная с последнего использованного.*/
    private static long idGenerator = 1;

    public Dragon(String name, Coordinates coordinates, long age, String description,
           DragonType dragonType, DragonCharacter dragonCharacter, Person killer){
        this(name, coordinates, age, description, dragonType, dragonCharacter,
                killer, idGenerator++, java.time.ZonedDateTime.now());
    }

    /** Устанавливает значения, начиная с которого будут генерироваться id элементов коллекции*/
    public static void setIdGenerator(long idGenerator) {
        Dragon.idGenerator = idGenerator;
    }

    @Override
    public int compareTo(Dragon o) {
        return Long.compare(o.id(), id);
    }
}
