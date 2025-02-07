package ru.itmo.java.homoursus.laba5.commands;

import ru.itmo.java.homoursus.laba5.App;

public class PrintFieldDescendingKiller implements ICommand {
    @Override
    public String getDescription() {
        return "вывести значения поля killer всех элементов в порядке убывания";
    }

    @Override
    public void execute(App app, String[] args) {

    }
}
