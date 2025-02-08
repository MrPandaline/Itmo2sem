package ru.itmo.java.homoursus.laba5.logic;

import ru.itmo.java.homoursus.laba5.commands.ICommand;

import java.util.HashMap;

public class CommandManager {
    private HashMap<String, ICommand> commands = new HashMap<>();

    public void addCommand(String commandName, ICommand command) {
        this.commands.put(commandName, command);
    }

    //TODO: добавить обработку случая, когда команда не найдена
    public ICommand getCommandByName(String commandName) {
        return this.commands.get(commandName);
    }

    public ICommand[] getCommands() {
        return this.commands.values().toArray(new ICommand[0]);
    }

    public String[] getCommandNames() {
        return this.commands.keySet().toArray(new String[0]);
    }
}
