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

public class App {
    private final IIOManager ioManager;
    private final CommandManager commandManager;
    private final CollectionManager<Dragon> collectionManager;
    private final IModelStorageManager storageManager;
    private final ArrayList<String> lastUsedCommands;

    private boolean isAppWorking;

    {
        isAppWorking = true;
    }

    public App(IModelStorageManager storageManager, IIOManager ioManager) {
        this.ioManager = ioManager;
        this.storageManager = storageManager;
        this.lastUsedCommands = CommandsListStoragingManager.readFromStorage(ioManager);

        this.collectionManager = new CollectionManager<>(new LinkedList<>());
        Dragon.setIdGenerator(storageManager.getNextID(ioManager));
        collectionManager.setCollection(storageManager.readFromStorage(ioManager));
        Collections.sort(collectionManager.getCollection());
        this.commandManager = new CommandManager();
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
    }

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

    public CollectionManager<Dragon> getCollectionManager() {
        return collectionManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public IIOManager getIoManager() {
        return ioManager;
    }

    public IModelStorageManager getStorageManager() {
        return storageManager;
    }

    public ArrayList<String> getLastUsedCommands() {
        return lastUsedCommands;
    }

    public void turnOffApp(){
        CommandsListStoragingManager.writeToStorage(lastUsedCommands);
        isAppWorking = false;
    }
}

