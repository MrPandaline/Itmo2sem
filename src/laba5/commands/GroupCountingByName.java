package laba5.commands;

import laba5.App;
import laba5.input.IIOManager;
import laba5.model.Dragon;

import java.util.Comparator;
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
        return "Сгруппировать элементы коллекции по значению поля name, \nВывести количество элементов в каждой группе";
    }

    @Override
    public void execute(App app, String[] args) {
        IIOManager ioManager = app.getIoManager();
        LinkedList<Dragon> linkedList = app.getCollectionManager().getCollection();
        linkedList.sort(Comparator.comparing(Dragon::name));

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
