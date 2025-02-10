package ru.itmo.java.homoursus.laba5.logic;

import ru.itmo.java.homoursus.laba5.commands.ICommand;
import ru.itmo.java.homoursus.laba5.exceptions.CommandNotFound;

import java.util.HashMap;

/**
 * Класс, управляющий командами.
 * @author Homoursus
 * @version 1.0
 */
public class CommandManager {
    /** Хэш-таблица, хранящая пары название команды: ссылка на объект команды.*/
    private final HashMap<String, ICommand> commands = new HashMap<>();

    /**
     * Метод, добавляющий команду в хэш-таблицу.
     * @param command Класс команды.
     * @param commandName Строка, необходимую внести пользователю, для выполнения команды.
     * */
    public void addCommand(String commandName, ICommand command) {
        this.commands.put(commandName, command);
    }

    /**
     * Метод, возвращающий команду, по её названию.
     * @param commandName Строка, необходимую внести пользователю, для выполнения команды.
     * @throws CommandNotFound ошибка, показывающая, что команда не найдена.
     * */
    public ICommand getCommandByName(String commandName) throws CommandNotFound {
        if (commands.containsKey(commandName)) {
            return commands.get(commandName);
        }
        else {
            throw new CommandNotFound();
        }
    }

    /** @return массив объектов используемых команд.*/
    public ICommand[] getCommands() {
        return this.commands.values().toArray(new ICommand[0]);
    }

    /** @return массив названий команд.*/
    public String[] getCommandNames() {
        return this.commands.keySet().toArray(new String[0]);
    }
}
