package ru.itmo.java.homoursus.laba5.logic;

import java.time.ZonedDateTime;
import java.util.LinkedList;

/**
 * Обобщённый класс - менеджер управления коллекцией.
 * @author Homoursus
 * @version 1.0.1
 */
public class CollectionManager <T> {

    /** Коллекция, данная по заданию.*/
    private LinkedList<T> collection;

    /** Дата инициализации коллекции. Обновляется каждый раз при запуске приложения.*/
    private final java.time.ZonedDateTime initializationTime;

    {
        initializationTime = java.time.ZonedDateTime.now();
    }

    /**
     * Конструктор класса менеджера коллекции.
     * @param collection коллекция, данная по заданию.
     * */
    public CollectionManager(LinkedList<T> collection) {
        this.collection = collection;
    }

    /** @return возвращает используемую коллекцию.*/
    public LinkedList<T> getCollection() {
        return collection;
    }

    /** Метод, позволяющий заменить используемую коллекцию.*/
    public void setCollection(LinkedList<T> collection) {
        this.collection = collection;
    }

    /** @return дату и время инициализации коллекции.*/
    public ZonedDateTime getCollectionInitializationTime() {
        return initializationTime;
    }

    /** Возвращает класс используемой коллекции. */
    public Class<?> getCollectionType() {
        return collection.getClass();
    }
}
