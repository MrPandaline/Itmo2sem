package ru.itmo.java.homoursus.laba5.commands;

import ru.itmo.java.homoursus.laba5.App;

public class Save implements ICommand{
    @Override
    public String getDescription() {
        return "Сохранить коллекцию в файл";
    }

    @Override
    public void execute(App app, String[] args) {

    }
}
