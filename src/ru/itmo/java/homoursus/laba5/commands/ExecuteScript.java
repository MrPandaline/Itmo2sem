package ru.itmo.java.homoursus.laba5.commands;

import ru.itmo.java.homoursus.laba5.App;

import java.util.Collection;

public class ExecuteScript implements ICommand{
    @Override
    public String getDescription() {
        return "считать и исполнить скрипт из указанного файла.";
    }

    @Override
    public void execute(App app, String[] args) {

    }

}
