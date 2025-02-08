package ru.itmo.java.homoursus.laba5.commands;

import ru.itmo.java.homoursus.laba5.App;
import ru.itmo.java.homoursus.laba5.input.IIOManager;
import ru.itmo.java.homoursus.laba5.logic.ModelHandler;
import ru.itmo.java.homoursus.laba5.model.Dragon;

import java.util.LinkedList;

public class RemoveGreater implements ICommand{
    @Override
    public String getDescription() {
        return "удалить из коллекции все элементы, превышающие заданный";
    }

    @Override
    public void execute(App app, String[] args) {
        IIOManager ioManager = app.getInput();
        LinkedList<Dragon> linkedList = app.getCollectionManager().getCollection();
        Dragon curDragon = new ModelHandler(ioManager).handleDragon();
        for (Dragon dragon : linkedList) {
            if (curDragon.compareTo(dragon) < 0) {
                linkedList.remove(dragon);
            }
        }
    }
}
