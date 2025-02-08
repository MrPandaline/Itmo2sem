package ru.itmo.java.homoursus.laba5.commands;

import ru.itmo.java.homoursus.laba5.App;

import java.util.LinkedList;

public class Clear implements ICommand{
    @Override
    public String getDescription() {
        return "очистить коллекцию";
    }

    @Override
    public void execute(App app, String[] args) {
        app.getCollectionManager().setCollection(new LinkedList<>());
        app.getInput().writeMessage("Коллекция очищена!");
    }
}
