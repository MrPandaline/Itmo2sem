package ru.itmo.java.homoursus.laba5.commands;

import ru.itmo.java.homoursus.laba5.App;

import java.util.Collection;

public class Info implements ICommand{
    @Override
    public String getDescription() {
        return "вывод информации о коллекции";
    }

    @Override
    public void execute(App app, String[] args) {

    }
}
