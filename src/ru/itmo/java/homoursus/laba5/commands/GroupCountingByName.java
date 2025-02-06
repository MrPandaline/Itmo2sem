package ru.itmo.java.homoursus.laba5.commands;

public class GroupCountingByName implements ICommand{
    @Override
    public String getDescription() {
        return "сгруппировать элементы коллекции по значению поля name, вывести количество элементов в каждой группе";
    }

    @Override
    public void execute() {

    }
}
