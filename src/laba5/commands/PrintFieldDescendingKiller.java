package laba5.commands;

import laba5.App;
import laba5.model.Dragon;

import java.util.Collections;
import java.util.LinkedList;

/**
 * Класс команды, реализующий вывод элементов коллекции в порядке убывания значения поля killer.
 * @author Homoursus
 * @version 1.1
 */
public class PrintFieldDescendingKiller implements ICommand {
    @Override
    public String getDescription() {
        return "Вывести значения поля killer всех элементов в порядке убывания";
    }

    @Override
    public void execute(App app, String[] args) {
        LinkedList<Dragon> collection = app.getCollectionManager().getCollection();
        collection.sort((a, b) -> {
            if (b.killer() != null && a.killer() != null) {
                return b.killer().compareTo(a.killer());
            }
            else {
                return a.name().compareTo(b.name());
            }
            });
        for (Dragon dragon : collection) {
            if (dragon.killer() != null) {
                app.getIoManager().writeMessage(dragon.killer().toString() + "\n");
                app.getIoManager().writeMessage("\n");
            }
        }
        Collections.sort(collection);
    }
}
