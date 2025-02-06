package ru.itmo.java.homoursus.laba5.commands;

public class Exit implements ICommand{
    @Override
    public String getDescription() {
        return "завершить программу (без сохранения в файл)";
    }

    @Override
    public void execute() {

    }
}
