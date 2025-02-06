package ru.itmo.java.homoursus.laba5.commands;

public class History implements ICommand{
    @Override
    public String getDescription() {
        return "вывести последние 15 команд";
    }

    @Override
    public void execute() {

    }
}
