package ru.itmo.java.homoursus.laba5.commands;

import ru.itmo.java.homoursus.laba5.App;
import ru.itmo.java.homoursus.laba5.input.IIOManager;
import ru.itmo.java.homoursus.laba5.model.Dragon;
import ru.itmo.java.homoursus.laba5.model.modelEnums.DragonType;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class FilterGreaterThanType implements ICommand{
    @Override
    public String getDescription() {
        return "вывести элементы, значение поля type которых, больше заданного";
    }

    @Override
    public void execute(App app, String[] args) {
        IIOManager ioManager = app.getInput();
        LinkedList<Dragon> linkedList = app.getCollectionManager().getCollection();
        if (args.length == 0) {
            ioManager.writeMessage("Вы не ввели значение поля type! ");
        } else {
            DragonType dragonType = DragonType.valueOf(args[0]);
            linkedList.sort((a, b) -> a.dragonType().compareTo(b.dragonType()));
            boolean flag = false;
            for (Dragon dragon : linkedList) {
                if (dragon.dragonType() == dragonType || flag) {
                    flag = true;
                    ioManager.writeMessage(dragon.toString());
                }
            }
        }
    }
}
