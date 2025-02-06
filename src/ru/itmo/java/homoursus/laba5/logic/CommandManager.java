package ru.itmo.java.homoursus.laba5.logic;


import ru.itmo.java.homoursus.laba5.commands.ICommand;

import java.util.HashMap;

public class CommandManager {
    HashMap<String, ICommand> commands = new HashMap<String, ICommand>();

    public void addCommand(String commandName, ICommand command) {
        this.commands.put(commandName, command);
    }

    //TODO: добавить обработку случая, когда команда не найдена
    public ICommand getCommand(String commandName) {
        return this.commands.get(commandName);
    }
}
