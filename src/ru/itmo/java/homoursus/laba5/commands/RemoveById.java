package ru.itmo.java.homoursus.laba5.commands;

import ru.itmo.java.homoursus.laba5.App;
import ru.itmo.java.homoursus.laba5.input.IIOManager;
import ru.itmo.java.homoursus.laba5.model.Dragon;

import java.util.LinkedList;

/**
 * Класс команды, реализующий удаление элемента коллекции по его id.
 * @author Homoursus
 * @version 1.0
 */
public class RemoveById implements ICommand{
    @Override
    public String getDescription() {
        return "удалить элемент из коллекции по его id";
    }

    @Override
    public void execute(App app, String[] args) {
        IIOManager ioManager = app.getIoManager();
        LinkedList<Dragon> linkedList = app.getCollectionManager().getCollection();
        if (args.length == 0) {
            ioManager.writeMessage("Вы не ввели id элемента коллекции!");
        } else {
            int id = Integer.parseInt(args[0]);
            linkedList.removeIf(dragon -> dragon.id() == id);
        }
    }
}
