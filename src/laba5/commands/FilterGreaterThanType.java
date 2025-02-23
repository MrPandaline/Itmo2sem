package laba5.commands;

import laba5.App;
import laba5.input.IIOManager;
import laba5.model.Dragon;
import laba5.model.modelEnums.DragonType;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

/**
 * Класс команды, реализующий вывод элементов коллекции, значение поля type которых, передано в команду.
 * @author Homoursus
 * @version 1.0.1
 */
public class FilterGreaterThanType implements ICommand{
    @Override
    public String getDescription() {
        return "Вывести элементы, значение поля type которых, больше заданного\n" +
                "Требует ввода значения поля type";
    }

    @Override
    public void execute(App app, String[] args) {
        IIOManager ioManager = app.getIoManager();

        LinkedList<Dragon> linkedList = app.getCollectionManager().getCollection();
        if (args.length == 0) {
            annotate(ioManager);
        } else {
            try{
                DragonType dragonType = DragonType.valueOf(args[0].toUpperCase());
                linkedList.sort(Comparator.comparing(Dragon::dragonType));
                boolean flag = false;
                for (Dragon dragon : linkedList) {
                    if (dragon.dragonType().compareTo(dragonType) > 0) {
                        flag = true;
                        ioManager.writeMessage(dragon + "\n");
                    }
                }
                if (!flag) {
                    ioManager.writeMessage("Нет элементов коллекции, чьё значение пол type превышает заданное!\n");
                }
                Collections.sort(linkedList);
            }
            catch (IllegalArgumentException e){
               annotate(ioManager);
            }

        }
    }
    private void annotate(IIOManager ioManager) {
        ioManager.writeMessage("Вы не верно ввели значение поля type! \n");

        ioManager.writeMessage("Доступные типы: " );
        for (DragonType dragonType : DragonType.values()) {
            ioManager.writeMessage(dragonType.toString() + " ");
        }
        ioManager.writeMessage("\n");
    }
}
