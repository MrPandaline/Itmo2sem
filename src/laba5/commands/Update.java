package laba5.commands;

import laba5.App;
import laba5.input.IIOManager;
import laba5.logic.ModelBuilder;
import laba5.model.Dragon;

import java.util.LinkedList;

/**
 * Класс команды, реализующий обновление элемента коллекции по id.
 * @author Homoursus
 * @version 1.0
 */
public class Update implements ICommand{
    @Override
    public String getDescription() {
        return "Позволяет обновить значение элемента коллекции, id которого равен заданному \nТребует ввода id";
    }

    @Override
    public void execute(App app, String[] args) {
        IIOManager ioManager = app.getIoManager();
        LinkedList<Dragon> linkedList = app.getCollectionManager().getCollection();
        if (args.length == 0) {
            ioManager.writeMessage("Вы не ввели id элемента коллекции!");
        }
        else{
            int id = Integer.parseInt(args[0]);
            boolean flag = false;
            for(Dragon dragon : linkedList){
                if (dragon.id() == id){
                    flag = true;
                    Dragon newDragon = new ModelBuilder(app.getIoManager()).buildDragon();
                    linkedList.set(linkedList.indexOf(dragon), newDragon);
                }
            }
            if (!flag){
                ioManager.writeMessage("Элемент коллекции с таким id не найден! \n" +
                        "Введите show, чтобы вывести список доступных элементов.\n");
            }

        }
    }
}
