package ru.itmo.java.homoursus.laba5.commands;

import ru.itmo.java.homoursus.laba5.App;
import ru.itmo.java.homoursus.laba5.model.Dragon;

import java.util.Collections;
import java.util.LinkedList;

/**
 * Класс команды, реализующий вывод элементов коллекции в порядке убывания значения поля killer.
 * @author Homoursus
 * @version 1.1
 */
public class PrintFieldDescendingKiller implements ICommand {
    @Override
    public String getDescription() {
        return "вывести значения поля killer всех элементов в порядке убывания";
    }

    @Override
    public void execute(App app, String[] args) {
        LinkedList<Dragon> collection = app.getCollectionManager().getCollection();
        collection.sort((a, b) -> b.killer().compareTo(a.killer()));
        for (Dragon dragon : collection) {
            app.getIoManager().writeMessage(dragon.killer().toString() + "\n");
        }
        Collections.sort(collection);
    }
}
