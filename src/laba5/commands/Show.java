package laba5.commands;

import laba5.App;
import laba5.input.IIOManager;

import java.util.LinkedList;

/**
 * Класс команды, реализующий вывод всех элементов коллекции.
 * @author Homoursus
 * @version 1.0
 */
public class Show implements ICommand{
    @Override
    public String getDescription() {
        return "Вывод всех элементов коллекции";
    }

    @Override
    public void execute(App app, String[] args) {
        LinkedList<?> collection = app.getCollectionManager().getCollection();
        IIOManager ioManager = app.getIoManager();
        if (!collection.isEmpty()) {
            for (Object object : collection) {
                ioManager.writeMessage(object.toString() + '\n' + '\n');
            }
        }
        else {
            ioManager.writeMessage("Коллекция пуста! Введите add для добавления нового элемента.");
        }
    }
}
