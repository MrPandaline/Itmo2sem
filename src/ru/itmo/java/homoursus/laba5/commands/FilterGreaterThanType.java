package ru.itmo.java.homoursus.laba5.commands;

import ru.itmo.java.homoursus.laba5.App;

public class FilterGreaterThanType implements ICommand{
    @Override
    public String getDescription() {
        return "вывести элементы, значение поля type которых, больше заданного";
    }

    @Override
    public void execute(App app, String[] args) {

    }
}
