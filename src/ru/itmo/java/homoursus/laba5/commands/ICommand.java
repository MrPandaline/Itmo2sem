package ru.itmo.java.homoursus.laba5.commands;

import ru.itmo.java.homoursus.laba5.App;

public interface ICommand {
    String getDescription();

    void execute(App app, String[] args);
}
