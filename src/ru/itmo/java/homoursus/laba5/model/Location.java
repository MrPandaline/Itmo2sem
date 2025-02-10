package ru.itmo.java.homoursus.laba5.model;

/**
 * Record, хранящий локацию. Используется в модели
 * @see Person
 * @param z Значение не может быть null
 * @version 1.0
 */
public record Location(float x, double y, Integer z) {
}
