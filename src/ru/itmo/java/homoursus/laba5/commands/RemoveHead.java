package ru.itmo.java.homoursus.laba5.commands;

import ru.itmo.java.homoursus.laba5.App;
import ru.itmo.java.homoursus.laba5.input.IIOManager;
import ru.itmo.java.homoursus.laba5.model.Dragon;

import java.util.LinkedList;

public class RemoveHead implements ICommand{
    @Override
    public String getDescription() {
        return "вывести из коллекции первый элемент и удалить его";
    }

    @Override
    public void execute(App app, String[] args) {
        LinkedList<Dragon> linkedList = app.getCollectionManager().getCollection();
        IIOManager ioManager = app.getInput();
        ioManager.writeMessage("Первый элемент коллекции: " + linkedList.poll() + "\n");
    }
}
