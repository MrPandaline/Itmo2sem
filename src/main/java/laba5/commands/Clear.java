package laba5.commands;

import laba5.App;

import java.util.LinkedList;

/**
 * Класс команды, реализующий очистку коллекции.
 * @author Homoursus
 * @version 1.0
 */
public class Clear implements ICommand{
    @Override
    public String getDescription() {
        return "Очистить коллекцию";
    }

    @Override
    public void execute(App app, String[] args) {
        app.getCollectionManager().setCollection(new LinkedList<>());
        app.getIoManager().writeMessage("Коллекция очищена!", outInQuiteMode);
    }
}
