package ru.itmo.java.homoursus.laba5.commands;

import ru.itmo.java.homoursus.laba5.App;
import ru.itmo.java.homoursus.laba5.logic.ModelBuilder;
import ru.itmo.java.homoursus.laba5.model.Dragon;

/**
 * Класс команды, реализующий добавление элементов в коллекцию.
 * @author Homoursus
 * @version 1.0
 */
public class Add implements ICommand {
    @Override
    public String getDescription() {
        return "добавить новый элемент в коллекцию";
    }

    @Override
    public void execute(App app, String[] args) {
        ModelBuilder handler = new ModelBuilder(app.getIoManager());
        Dragon dragon = handler.buildDragon();
        app.getCollectionManager().getCollection().add(dragon);
    }
}
