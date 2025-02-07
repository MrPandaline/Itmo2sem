package ru.itmo.java.homoursus.laba5.commands;

import ru.itmo.java.homoursus.laba5.App;

public class Help implements ICommand{
    @Override
    public String getDescription() {
        return "вывод справку по доступным командам";
    }

    @Override
    public void execute(App app, String[] args) {

    }
}
