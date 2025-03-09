package laba5.commands;

import laba5.App;
import laba5.input.IIOManager;
import laba5.model.Dragon;

import java.util.LinkedList;

/**
 * Класс команды, реализующий удаление элемента коллекции по его id.
 * @author Homoursus
 * @version 1.0
 */
public class RemoveById implements ICommand{
    @Override
    public String getDescription() {
        return "Позволят удалить элемент из коллекции по его id, \nТребует ввода id";
    }

    @Override
    public void execute(App app, String[] args) {
        IIOManager ioManager = app.getIoManager();
        LinkedList<Dragon> linkedList = app.getCollectionManager().getCollection();
        if (args.length == 0) {
            ioManager.writeMessage("Вы не ввели id элемента коллекции!\n", false);
        } else {
            int id = Integer.parseInt(args[0]);
            if (linkedList.removeIf(dragon -> dragon.id() == id)) {
                ioManager.writeMessage("Элемент коллекции удалён!\n", outInQuiteMode);
            }
            else {
                ioManager.writeMessage("Нет элементов коллекции с таким индексом!\n", outInQuiteMode);
            }
        }
    }
}
