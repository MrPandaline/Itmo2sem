package ru.itmo.java.homoursus.laba5.commands;

public class Help implements ICommand{
    @Override
    public String getDescription() {
        return "вывод справку по доступным командам";
    }

    @Override
    public void execute() {

    }
}
