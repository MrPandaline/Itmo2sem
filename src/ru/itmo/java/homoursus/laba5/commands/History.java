package ru.itmo.java.homoursus.laba5.commands;

import ru.itmo.java.homoursus.laba5.App;

import java.util.Collection;

public class History implements ICommand{
    @Override
    public String getDescription() {
        return "вывести последние 15 команд";
    }

    @Override
    public void execute(App app, String[] args) {
        for (String lastUsedCommand : app.getLastUsedCommands()) {
            app.getIoManager().writeMessage(lastUsedCommand + "\n");
        }
    }
}
