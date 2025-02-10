package ru.itmo.java.homoursus.laba5.commands;

import ru.itmo.java.homoursus.laba5.App;
import ru.itmo.java.homoursus.laba5.input.IIOManager;
import ru.itmo.java.homoursus.laba5.logic.CollectionManager;

/**
 * Класс команды, реализующий вывод информации о коллекции.
 * Выводит тип, время инициализации, размер и класс, экземпляры которого содержатся в коллекции.
 * @author Homoursus
 * @version 1.0
 */
public class Info implements ICommand{
    @Override
    public String getDescription() {
        return "вывод информации о коллекции";
    }

    @Override
    public void execute(App app, String[] args) {
        CollectionManager<?> collectionManager = app.getCollectionManager();
        IIOManager ioManager = app.getIoManager();
        ioManager.writeMessage("Тип коллекции: " + collectionManager.getCollectionType().getName() + "\n");
        ioManager.writeMessage("Время инициализации коллекции: " +
                collectionManager.getCollectionInitializationTime().toString() + "\n");
        ioManager.writeMessage("Размер коллекции: " + collectionManager.getCollection().size() + "\n");
        if (!collectionManager.getCollection().isEmpty()) {
            ioManager.writeMessage("Класс, экземпляры которого содержатся в коллекции: " +
                    collectionManager.getCollection().iterator().next().getClass().getName() + "\n");
        }
    }
}
