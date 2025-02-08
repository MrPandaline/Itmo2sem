package ru.itmo.java.homoursus.laba5.commands;

import ru.itmo.java.homoursus.laba5.App;

import java.util.LinkedList;

public class Show implements ICommand{
    @Override
    public String getDescription() {
        return "вывод всех элементов коллекции";
    }

    @Override
    public void execute(App app, String[] args) {
        LinkedList<?> collection = app.getCollectionManager().getCollection();
        for (Object object : collection) {
            System.out.println(object);
        }
    }
}
