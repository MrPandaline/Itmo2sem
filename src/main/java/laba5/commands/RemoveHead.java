package laba5.commands;

import laba5.App;
import laba5.input.IIOManager;
import laba5.model.Dragon;

import java.util.LinkedList;

/**
 * Класс команды, реализующий вывод первого элемента коллекции и его удаление.
 * @author Homoursus
 * @version 1.0
 */
public class RemoveHead implements ICommand{
    @Override
    public String getDescription() {
        return "Вывести из коллекции первый элемент и удалить его";
    }

    @Override
    public void execute(App app, String[] args) {
        LinkedList<Dragon> linkedList = app.getCollectionManager().getCollection();
        IIOManager ioManager = app.getIoManager();
        Dragon dragon = linkedList.poll();
        if (dragon != null) {
            ioManager.writeMessage("Первый элемент коллекции: \n" + dragon + "\n", outInQuiteMode);
        }
        else{
            ioManager.writeMessage("Коллекция пуста! Введите add для добавления нового элемента.", outInQuiteMode);
        }
    }
}
