package laba5.commands;

import laba5.App;
import laba5.input.IIOManager;
import laba5.logic.ModelBuilder;
import laba5.model.Dragon;

import java.util.LinkedList;

/**
 * Класс команды, реализующий удаление элементов коллекции, превышающих заданный.
 * @author Homoursus
 * @version 1.0.1
 */
public class RemoveGreater implements ICommand{
    @Override
    public String getDescription() {
        return "Позволяет удалить из коллекции все элементы, превышающие заданный\n" +
                "Требует ввода нового элемента коллекции";
    }

    @Override
    public void execute(App app, String[] args) {
        IIOManager ioManager = app.getIoManager();
        LinkedList<Dragon> linkedList = app.getCollectionManager().getCollection();
        Dragon curDragon = new ModelBuilder(ioManager).buildDragon();
        linkedList.removeIf(dragon -> curDragon.compareTo(dragon) < 0);
        ioManager.writeMessage("Элементы большие, чем введённый, удалены.", outInQuiteMode);
    }
}
