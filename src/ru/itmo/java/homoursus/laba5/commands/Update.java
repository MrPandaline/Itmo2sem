package ru.itmo.java.homoursus.laba5.commands;

import ru.itmo.java.homoursus.laba5.App;
import ru.itmo.java.homoursus.laba5.input.IIOManager;
import ru.itmo.java.homoursus.laba5.logic.CollectionManager;
import ru.itmo.java.homoursus.laba5.logic.ModelHandler;
import ru.itmo.java.homoursus.laba5.model.Dragon;

import java.util.LinkedList;

public class Update implements ICommand{
    @Override
    public String getDescription() {
        return "обновить значение элемента коллекции, id которого равен заданному";
    }

    @Override
    public void execute(App app, String[] args) {
        IIOManager ioManager = app.getInput();
        LinkedList<Dragon> linkedList = app.getCollectionManager().getCollection();
        if (args.length == 0) {
            ioManager.writeMessage("Вы не ввели id элемента коллекции!");
        }
        else{
            int id = Integer.parseInt(args[0]);
            for(Dragon dragon : linkedList){
                if (dragon.id() == id){
                    Dragon newDragon = new ModelHandler(app.getInput()).handleDragon();
                    linkedList.set(linkedList.indexOf(dragon), newDragon);
                }
            }

        }
    }
}
