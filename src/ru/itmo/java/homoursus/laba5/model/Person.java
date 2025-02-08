package ru.itmo.java.homoursus.laba5.model;

import ru.itmo.java.homoursus.laba5.model.Location;
import ru.itmo.java.homoursus.laba5.model.modelEnums.Color;
import ru.itmo.java.homoursus.laba5.model.modelEnums.Country;

public record Person(String name, int height, Color eyeColor, Color hairColor, Country nationality, Location location) {
}
