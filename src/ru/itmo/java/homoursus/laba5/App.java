package ru.itmo.java.homoursus.laba5;

import ru.itmo.java.homoursus.laba5.commands.*;
import ru.itmo.java.homoursus.laba5.exceptions.CommandNotFound;
import ru.itmo.java.homoursus.laba5.input.IIOManager;
import ru.itmo.java.homoursus.laba5.logic.CollectionManager;
import ru.itmo.java.homoursus.laba5.logic.CommandManager;
import ru.itmo.java.homoursus.laba5.model.Dragon;
import ru.itmo.java.homoursus.laba5.storage.CommandsListStoragingManager;
import ru.itmo.java.homoursus.laba5.storage.IModelStorageManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Класс, объединяющий все модули приложения.
 * @author Homoursus
 * @version 1.1
 */
public class App {
    /**
     * Ссылка на менеджер ввода вывода.
     * @see IIOManager
     * */
    private final IIOManager ioManager;

    /**
     * Ссылка на менеджер управления команд.
     * @see CommandManager
     * */
    private final CommandManager commandManager;

    /**
     * Ссылка на менеджер управления коллекцией.
     * @see CollectionManager
     * */
    private final CollectionManager<Dragon> collectionManager;

    /**
     * Ссылка на менеджер работы с записью коллекции в хранилище.
     * @see IModelStorageManager
     * */
    private final IModelStorageManager storageManager;

    /**
     * Ссылка на ArrayList последних использованных команд.
     * */
    private final ArrayList<String> lastUsedCommands;

    /**
     * Флаг состояния, показывающий, включено ли приложение.
     * */
    private boolean isAppWorking;

    /**
     * Название файла, в котором хранятся последние использованные команды.
     * */
    private final String commandsFileName;

    {
        isAppWorking = true;
    }

    /**
     * Конструктор приложения. Инициализирует всех менеджеров приложения.
     * @param ioManager класс-реализация менеджера управления вводом-выводом.
     * @param storageManager класс-реализация менеджера управления хранилищем.
     * @param commandsFileName название файла, в котором будут храниться последние использованные команды.
     * */
    public App(IModelStorageManager storageManager, IIOManager ioManager, String commandsFileName) {
        this.ioManager = ioManager;
        this.storageManager = storageManager;
        this.lastUsedCommands = new CommandsListStoragingManager(commandsFileName).readFromStorage(ioManager);
        this.commandsFileName = commandsFileName;

        this.collectionManager = new CollectionManager<>(new LinkedList<>());
        Dragon.setIdGenerator(storageManager.getNextID(ioManager));
        collectionManager.setCollection(storageManager.readFromStorage(ioManager));
        Collections.sort(collectionManager.getCollection());
        this.commandManager = this.buildCommandManager();
    }

    /**
     * Метод, инициализирующий менеджер управления команд.
     * */
    private CommandManager buildCommandManager() {
        CommandManager commandManager = new CommandManager();
        commandManager.addCommand("help", new Help());
        commandManager.addCommand("info", new Info());
        commandManager.addCommand("show", new Show());
        commandManager.addCommand("add", new Add());
        commandManager.addCommand("update", new Update());
        commandManager.addCommand("remove_by_id", new RemoveById());
        commandManager.addCommand("clear", new Clear());
        commandManager.addCommand("save", new Save());
        commandManager.addCommand("execute_script", new ExecuteScript());
        commandManager.addCommand("exit", new Exit());
        commandManager.addCommand("remove_head", new RemoveHead());
        commandManager.addCommand("remove_greater", new RemoveGreater());
        commandManager.addCommand("history", new History());
        commandManager.addCommand("group_counting_by_name", new GroupCountingByName());
        commandManager.addCommand("filter_greater_than_type", new FilterGreaterThanType());
        commandManager.addCommand("print_field_descending_killer", new PrintFieldDescendingKiller());
        return commandManager;
    }

    /**
     * Метод, запускающий приложение.
     */
    public void run(){
        String userInput;
        while (isAppWorking) {
            String[] args;
            try{
                userInput = ioManager.getRawInput().toLowerCase();
                args = userInput.split(" ");

                ICommand cmd = commandManager.getCommandByName(args[0]);
                cmd.execute(this, args);
                this.lastUsedCommands.add(userInput);
            }
            catch(CommandNotFound | NullPointerException e) {
                System.out.println("Команда не найдена! Введите help для получения списка доступных комманд");
            }
        }
    }

    /**
     * @return используемый менеджер управления коллекцией.
     * */
    public CollectionManager<Dragon> getCollectionManager() {
        return collectionManager;
    }

    /**
     * @return используемый менеджер управления командами.
     * */
    public CommandManager getCommandManager() {
        return commandManager;
    }

    /**
     * @return используемый менеджер управления вводом-выводом.
     * */
    public IIOManager getIoManager() {
        return ioManager;
    }

    /**
     * @return используемый менеджер управления командами.
     * */
    public IModelStorageManager getStorageManager() {
        return storageManager;
    }

    /**
     * @return ArrayList последних использованных команд.
     */
    public ArrayList<String> getLastUsedCommands() {
        return lastUsedCommands;
    }

    /**
     * Метод, используемый для прекращения работы приложения.
     */
    public void turnOffApp(){
        new CommandsListStoragingManager(commandsFileName).writeToStorage(lastUsedCommands);
        isAppWorking = false;
    }
}

