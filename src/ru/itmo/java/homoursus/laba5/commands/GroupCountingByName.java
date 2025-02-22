package ru.itmo.java.homoursus.laba5.commands;

import ru.itmo.java.homoursus.laba5.App;
import ru.itmo.java.homoursus.laba5.input.IIOManager;
import ru.itmo.java.homoursus.laba5.model.Dragon;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Класс команды, реализующий группировку элементов коллекции по имени дракона.
 * Выводит количество элементов в каждой группе.
 * @author Homoursus
 * @version 1.0
 */
public class GroupCountingByName implements ICommand{
    @Override
    public String getDescription() {
        return "сгруппировать элементы коллекции по значению поля name, вывести количество элементов в каждой группе";
    }

    @Override
    public void execute(App app, String[] args) {
        IIOManager ioManager = app.getIoManager();
        LinkedList<Dragon> linkedList = app.getCollectionManager().getCollection();
        linkedList.sort((a, b) -> a.name().compareTo(b.name()));

        HashMap<String, Integer> countMap = new HashMap<>();

        for (Dragon dragon : linkedList) {
            if (countMap.containsKey(dragon.name())) {
                countMap.put(dragon.name(), countMap.get(dragon.name()) + 1);
            }
            else{
                countMap.put(dragon.name(), 1);
            }
        }
        for (String name : countMap.keySet()) {
            ioManager.writeMessage("Элементов коллекции с именем " + name + ": " + countMap.get(name) + "\n");
        }
    }
}
