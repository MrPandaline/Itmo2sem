package ru.itmo.java.homoursus.laba5.commands;

import ru.itmo.java.homoursus.laba5.App;

public class RemoveHead implements ICommand{
    @Override
    public String getDescription() {
        return "вывести из коллекции первый элемент и удалить его";
    }

    @Override
    public void execute(App app, String[] args) {

    }
}
