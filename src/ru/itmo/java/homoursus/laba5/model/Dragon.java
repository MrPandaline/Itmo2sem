package ru.itmo.java.homoursus.laba5.model;

import ru.itmo.java.homoursus.laba5.model.modelEnums.DragonCharacter;
import ru.itmo.java.homoursus.laba5.model.modelEnums.DragonType;

public record Dragon(String name, Coordinates coordinates, long age, String description,
                     DragonType dragonType, DragonCharacter dragonCharacter, Person person,
                     long id, java.time.ZonedDateTime creationDate) implements Comparable<Dragon> {
    private static long idGenerator = 1;

    public Dragon(String name, Coordinates coordinates, long age, String description,
           DragonType dragonType, DragonCharacter dragonCharacter, Person person){
        this(name, coordinates, age, description, dragonType, dragonCharacter,
                person, idGenerator++, java.time.ZonedDateTime.now());
    }


    @Override
    public int compareTo(Dragon o) {
        return Long.compare(o.id(), id);
    }
}
