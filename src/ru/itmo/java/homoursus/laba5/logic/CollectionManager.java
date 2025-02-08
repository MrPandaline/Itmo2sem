package ru.itmo.java.homoursus.laba5.logic;

import java.time.ZonedDateTime;
import java.util.LinkedList;

public class CollectionManager <T> {

    private LinkedList<T> collection;
    private java.time.ZonedDateTime initializationTime = java.time.ZonedDateTime.now();

    {
        initializationTime = java.time.ZonedDateTime.now();
    }

    public CollectionManager(LinkedList<T> collection) {
        this.collection = collection;
    }

    public LinkedList<T> getCollection() {
        return collection;
    }

    public void setCollection(LinkedList<T> collection) {
        this.collection = collection;
    }

    public ZonedDateTime getCollectionInitializationTime() {
        return initializationTime;
    }

    public Class<?> getCollectionType() {
        return collection.getClass();
    }
}
