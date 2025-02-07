package ru.itmo.java.homoursus.laba5.logic;

import ru.itmo.java.homoursus.laba5.model.Dragon;

import java.util.Collection;

public class CollectionManager <T> {

    private Collection<T> collection;

    public CollectionManager(Collection<T> collection) {
        this.collection = collection;
    }

}
