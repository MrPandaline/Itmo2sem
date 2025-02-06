package ru.itmo.java.homoursus.laba5.commands;

public class Update implements ICommand{
    @Override
    public String getDescription() {
        return "обновить значение элемента коллекции, id которого равен заданному";
    }

    @Override
    public void execute() {

    }
}
