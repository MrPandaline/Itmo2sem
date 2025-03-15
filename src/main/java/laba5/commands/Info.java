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
        ioManager.printMessage("Тип коллекции: " + collectionManager.getCollectionType().getSimpleName() + "\n", outInQuiteMode);
        ioManager.printMessage("Время инициализации коллекции: " +
                time.toString().substring(0, 10) +" " + time.toString().substring(11, 19) + "\n", outInQuiteMode);
        ioManager.printMessage("Размер коллекции: " + collectionManager.getCollection().size() + "\n", outInQuiteMode);
        if (!collectionManager.getCollection().isEmpty()) {
            ioManager.printMessage("Класс, экземпляры которого содержатся в коллекции: " +
                    collectionManager.getCollection().iterator().next().getClass().getSimpleName() + "\n", outInQuiteMode);
        }
    }
}
