package ru.itmo.java.homoursus.laba5.commands;

import ru.itmo.java.homoursus.laba5.App;

public class Show implements ICommand{
    @Override
    public String getDescription() {
        return "вывод всех элементов коллекции";
    }

    @Override
    public void execute(App app, String[] args) {

    }
}
