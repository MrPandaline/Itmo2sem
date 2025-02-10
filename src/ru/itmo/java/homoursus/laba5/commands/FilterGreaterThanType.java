package ru.itmo.java.homoursus.laba5.commands;

import ru.itmo.java.homoursus.laba5.App;
import ru.itmo.java.homoursus.laba5.input.IIOManager;
import ru.itmo.java.homoursus.laba5.model.Dragon;
import ru.itmo.java.homoursus.laba5.model.modelEnums.DragonType;

import java.util.Collections;
import java.util.LinkedList;

/**
 * Класс команды, реализующий вывод элементов коллекции, значение поля type которых, передано в команду.
 * @author Homoursus
 * @version 1.0.1
 */
public class FilterGreaterThanType implements ICommand{
    @Override
    public String getDescription() {
        return "вывести элементы, значение поля type которых, больше заданного";
    }

    @Override
    public void execute(App app, String[] args) {
        IIOManager ioManager = app.getIoManager();
        LinkedList<Dragon> linkedList = app.getCollectionManager().getCollection();
        if (args.length == 1) {
            ioManager.writeMessage("Вы не ввели значение поля type! ");
        } else {
            DragonType dragonType = DragonType.valueOf(args[1]);
            linkedList.sort((a, b) -> a.dragonType().compareTo(b.dragonType()));
            boolean flag = false;
            for (Dragon dragon : linkedList) {
                if (dragon.dragonType() == dragonType || flag) {
                    flag = true;
                    ioManager.writeMessage(dragon + "\n");
                }
            }
            Collections.sort(linkedList);
        }
    }
}
