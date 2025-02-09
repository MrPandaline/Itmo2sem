package ru.itmo.java.homoursus.laba5.storage;
import ru.itmo.java.homoursus.laba5.input.IIOManager;
import ru.itmo.java.homoursus.laba5.model.Dragon;

import java.util.LinkedList;

public interface IModelStorageManager {
    void writeToStorage(LinkedList<Dragon> collection);

    LinkedList<Dragon> readFromStorage(IIOManager ioManager);

    long getNextID(IIOManager ioManager);
}
