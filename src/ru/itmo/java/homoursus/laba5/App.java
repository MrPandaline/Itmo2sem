package ru.itmo.java.homoursus.laba5;

import ru.itmo.java.homoursus.laba5.commands.*;
import ru.itmo.java.homoursus.laba5.input.ConsoleInputManager;
import ru.itmo.java.homoursus.laba5.input.IInputManager;
import ru.itmo.java.homoursus.laba5.logic.CommandManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class App {
    IInputManager input;
    private boolean isAppWorking;

    {
        isAppWorking = true;
    }

    public App() {
        this.input = new ConsoleInputManager(new BufferedReader(new InputStreamReader(System.in)));

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
    }

    public void run(){
        String userInput;
        while (isAppWorking) {
            userInput = input.getInput();
            System.out.println(userInput);

        }
    }
}
