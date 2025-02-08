package ru.itmo.java.homoursus.laba5;

import ru.itmo.java.homoursus.laba5.commands.*;
import ru.itmo.java.homoursus.laba5.input.ConsoleIOManager;
import ru.itmo.java.homoursus.laba5.input.IIOManager;
import ru.itmo.java.homoursus.laba5.logic.CollectionManager;
import ru.itmo.java.homoursus.laba5.logic.CommandManager;
import ru.itmo.java.homoursus.laba5.model.Dragon;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class App {
    private IIOManager input;
    private CommandManager commandManager;
    private CollectionManager<Dragon> collection;
    private boolean isAppWorking;

    {
        isAppWorking = true;
    }

    public App(LinkedList<Dragon> collection) {
        this.input = new ConsoleIOManager(new BufferedReader(new InputStreamReader(System.in)));

        this.collection = new CollectionManager<>(collection);
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
            userInput = input.getInput();
            String[] args = null;
            if (!userInput.isEmpty()) {
                args = userInput.split(" ");
            }
            //TODO: сделать нормальную обработку null через исключения
            ICommand cmd = commandManager.getCommandByName(args[0]);

            if (cmd != null){
                //TODO: передавать не весь список а срез с первого элемента
                cmd.execute(this, args);
            }
            else {
                System.out.println("Команда не найдена: " + args[0] +
                        ", введите help для получения списка доступных комманд");
            }
        }
    }

    public CollectionManager<Dragon> getCollectionManager() {
        return collection;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public IIOManager getInput() {
        return input;
    }

    public void turnOffApp(){
        isAppWorking = false;
    }
}

