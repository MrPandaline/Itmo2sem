package laba5.commands;

import laba5.App;
import laba5.input.IIOManager;
import laba5.logic.CollectionManager;

import java.time.ZonedDateTime;

/**
 * Класс команды, реализующий вывод информации о коллекции.
 * Выводит тип, время инициализации, размер и класс, экземпляры которого содержатся в коллекции.
 * @author Homoursus
 * @version 1.0
 */
public class Info implements ICommand{
    @Override
    public String getDescription() {
        return "Вывод информации о коллекции";
    }

    @Override
    public void execute(App app, String[] args) {
        CollectionManager<?> collectionManager = app.getCollectionManager();
        ZonedDateTime time = collectionManager.getCollectionInitializationTime();
        IIOManager ioManager = app.getIoManager();
        ioManager.writeMessage("Тип коллекции: " + collectionManager.getCollectionType().getSimpleName() + "\n");
        ioManager.writeMessage("Время инициализации коллекции: " +
                time.toString().substring(0, 10) +" " + time.toString().substring(11, 19) + "\n");
        ioManager.writeMessage("Размер коллекции: " + collectionManager.getCollection().size() + "\n");
        if (!collectionManager.getCollection().isEmpty()) {
            ioManager.writeMessage("Класс, экземпляры которого содержатся в коллекции: " +
                    collectionManager.getCollection().iterator().next().getClass().getSimpleName() + "\n");
        }
    }
}
