package ru.itmo.java.homoursus.laba5;

import ru.itmo.java.homoursus.laba5.model.Coordinates;
import ru.itmo.java.homoursus.laba5.model.Person;
import ru.itmo.java.homoursus.laba5.model.modelEnums.DragonCharacter;
import ru.itmo.java.homoursus.laba5.model.modelEnums.DragonType;


public class Dragon {
    static private long idGenerator = 1;
    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private long age; //Значение поля должно быть больше 0
    private String description; //Поле может быть null
    private DragonType type; //Поле может быть null
    private DragonCharacter character; //Поле не может быть null
    private Person killer; //Поле может быть null
    
    { 
        this.id = idGenerator++;
        this.creationDate = java.time.ZonedDateTime.now();
    }
    
    public Dragon(String name, Coordinates coordinates, long age, String description, DragonType type, DragonCharacter character, Person killer) {
        this.name = name;
        this.coordinates = coordinates;
        this.age = age;
        this.description = description;
        this.type = type;
        this.character = character;
        this.killer = killer;
        
    }   
}