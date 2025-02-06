package ru.itmo.java.homoursus.laba5.model;

import ru.itmo.java.homoursus.laba5.model.Location;
import ru.itmo.java.homoursus.laba5.model.modelEnums.Color;
import ru.itmo.java.homoursus.laba5.model.modelEnums.Country;


public class Person {
    private String name; //Поле не может быть null, Строка не может быть пустой
    private int height; //Значение поля должно быть больше 0
    private Color eyeColor; //Поле не может быть null
    private Color hairColor; //Поле не может быть null
    private Country nationality; //Поле не может быть null
    private Location location; //Поле не может быть null
    
    public Person(String name, int height, Color eyeColor, Color hairColor, Country nationality, Location location) {
    this.name = name;
    this.height = height;
    this.eyeColor = eyeColor;
    this.hairColor = hairColor;
    this.nationality = nationality;
    this.location = location;     
    }
}