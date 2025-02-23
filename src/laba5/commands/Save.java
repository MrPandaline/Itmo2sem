package laba5.commands;

import laba5.App;
import laba5.model.Dragon;
import laba5.storage.IModelStorageManager;

import java.util.LinkedList;

/**
 * Класс команды, реализующий сохранение элементов коллекции в хранилище.
 * @author Homoursus
 * @version 1.0
 */
public class Save implements ICommand{
    @Override
    public String getDescription() {
        return "Сохранить коллекцию в хранилище";
    }

    @Override
    public void execute(App app, String[] args) {
        LinkedList<Dragon> collection = app.getCollectionManager().getCollection();
        IModelStorageManager storageManager = app.getStorageManager();
        storageManager.writeToStorage(collection);
        app.getIoManager().writeMessage("Коллекция сохранена в файл! \n");
    }
}
