package ru.itmo.java.homoursus.laba5.commands;

import ru.itmo.java.homoursus.laba5.App;
import ru.itmo.java.homoursus.laba5.model.Dragon;

import java.util.LinkedList;

public class PrintFieldDescendingKiller implements ICommand {
    @Override
    public String getDescription() {
        return "вывести значения поля killer всех элементов в порядке убывания";
    }

    @Override
    public void execute(App app, String[] args) {
        LinkedList<Dragon> collection = app.getCollectionManager().getCollection();
        collection.sort((a, b) -> a.name().compareTo(b.name()));
    }
}
